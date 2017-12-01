package com.nerf.facerecognition;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Camera
{
	// path where to save the pictures
	private Path path;

	public Camera(Path path)
	{
		this.path = path;
	}

	public File takePicture() throws IOException, InterruptedException
	{
		Files.deleteIfExists(path);
		List<String> command = Arrays.asList("raspistill", "-w", "1024", "-h", "768", "-n", "-o", path.toString());
		Process process = new ProcessBuilder("raspistill").command(command).start();
		process.waitFor();
		return path.toFile();
	}
}
