package main

// inspired from github.com/speps/grumpy-pi-mjpg

import (
	"log"
	"net/http"
	"sync"
	"io"
	"os"
	"bytes"
	"fmt"
)

var (
	mutex = &sync.Mutex{}
	lastImageLen = 0
	lastImage = make([]byte, 1024*1024)
	magic = []byte{0xff, 0xd8, 0xff, 0xe0, 0x00, 0x10, 0x4a, 0x46, 0x49, 0x46, 0x00}
	work = make([]byte, len(magic))
	buffer = new(bytes.Buffer)
)

func main() {
	fmt.Println("run: raspvid -cd MJPEG -w 800 -h 600 -fps 10 -t 0 -n -o - | ./camera-service")
	http.HandleFunc("/picture", func(w http.ResponseWriter, r *http.Request) {
		fmt.Printf("serving image (%d)\n", lastImageLen)
		mutex.Lock()
		w.Header().Set("Content-Type", "image/jpeg")
		io.Copy(w, bytes.NewReader(lastImage[:lastImageLen]))
		mutex.Unlock()
	})

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Get a picture at /picture")
	})

	go parseStdin()
	
	port := ":8081"
	fmt.Printf("Start camera-service at port %s\n", port)
	log.Fatal(http.ListenAndServe(port, nil))
}

func parseStdin() {
	readBuf := make([]byte, 4096)
	for {
		n, err := os.Stdin.Read(readBuf)
		if err != nil {
			fmt.Fprintf(os.Stderr, "error reading stdin %v\n", err)
			break
		}
		
		processData(readBuf, n, func(image []byte) {
			mutex.Lock()
			lastImageLen = copy(lastImage, image)
			mutex.Unlock()
		})
	}
}

func processData(data []byte, n int, saveImage func(image []byte)) {
	start := 0
	for i := 0; i < n; i++ {
		work = work[1:]
		work = append(work, data[i])
		if bytes.Compare(work, magic) == 0 {
			buffer.Write(data[start:i])
			if buffer.Len() > 0 {
				end := buffer.Len() - len(magic) + 1
				image := buffer.Bytes()[:end]
				rest := buffer.Bytes()[end:]
				saveImage(image)
				buffer.Reset()
				buffer.Write(rest)
				start = i
			}
		}
	}
	buffer.Write(data[start:n])
}
