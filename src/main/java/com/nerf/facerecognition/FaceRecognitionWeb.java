package com.nerf.facerecognition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FaceRecognitionWeb
{
	public static void main(String[] argv) throws IOException, InterruptedException
	{
		FaceRecognitionService service = new FaceRecognitionService("10.4.48.239", 8080);
		Camera camera = new Camera(Paths.get("tmpstill.jpg"));
		while (true)
		{
			File file = camera.takePicture();
			DetectResult result = service.detect(file);
			System.out.printf("face count: %d\n", result.getFacesCount());
		}



	}

}
