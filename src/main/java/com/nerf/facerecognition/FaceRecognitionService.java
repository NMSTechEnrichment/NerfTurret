package com.nerf.facerecognition;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Manage the communication with the Facebox service
 *
 * TODO determine input depending on how we get the picture from the webcam
 */
@SuppressWarnings("WeakerAccess")
public class FaceRecognitionService
{
	/** address to connect to */
	private final String url;

	/** TEST */
	public static void main(String[] argv) throws IOException
	{
		FaceRecognitionService service = new FaceRecognitionService("10.4.48.239", 8080);
		service.train(new File("C:\\piproject\\2faces.jpg"));
		DetectResult result = service.detect(new File("C:\\piproject\\2faces.jpg"));
		//File image = new File("C:\\piproject\\faces\\image_0001.jpg");
		//File image = new File("C:\\piproject\\2faces.jpg");
		//File image = new File("C:\\piproject\\galaxy.jpg");
		System.out.println("DONE");
	}

	/**
	 *
	 * @param ipAddress ip address or name of server (e.g. 10.0.0.4)
	 * @param port port to connect to
	 */
	public FaceRecognitionService(String ipAddress, int port)
	{
		this.url = String.format("http://%s:%d", ipAddress, port);
	}

	/**
	 * Train the service to recognize a new face
	 */
	public void train(File image) throws IOException
	{
		try (
			FileInputStream fis = new FileInputStream(image);
			CloseableHttpClient httpClient = HttpClientBuilder.create().build()
		)
		{
			HttpEntity httpEntity = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addTextBody("name", "Toni")
				.addTextBody("id", "Toni.jpg")
				.addPart("file", new InputStreamBody(fis, image.getName()))
				.build();

			HttpPost httpPost = new HttpPost(url + "/facebox/teach");
			httpPost.setEntity(httpEntity);

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");

			System.out.println("[" + statusCode + "] " + responseString);
		}
	}

	/**
	 * Identify faces in an image
	 */
	public DetectResult detect(File image) throws IOException
	{
		try (
				FileInputStream fis = new FileInputStream(image);
				CloseableHttpClient httpClient = HttpClientBuilder.create().build()
		)
		{
			HttpEntity httpEntity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addTextBody("name", "Toni")
					.addTextBody("id", "Toni.jpg")
					.addPart("file", new InputStreamBody(fis, image.getName()))
					.build();

			HttpPost httpPost = new HttpPost(url + "/facebox/check");
			httpPost.setEntity(httpEntity);

			HttpResponse response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");

			System.out.println("[" + statusCode + "] " + responseString);
			return DetectResult.valueOf(responseString);
		}
	}
}
