package com.nerf.facerecognition;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Manage the communication with the Facebox service
 *
 */
@SuppressWarnings("WeakerAccess")
public class FaceRecognitionService
{
	public static final String IDENTIFIER = "Face";

	private static final Path FACES_FOLDER = Paths.get("/FacesCache");

	//private final URL PI_CAMERA = new URL("http://localhost:8081/picture");
	private final URL PI_CAMERA = new URL("http://192.168.3.4:8081/picture");

	/** address to connect to */
	private final String url;

	/** Pictures to take hoping there is a face in it before giving up */
	private final int takePictureRetries;

	private Image currentImage;

	/**
	 *
	 * @param retries pictures to take on web request for an image to find a face
	 * @param ipAddress ip address or name of server (e.g. 10.0.0.4)
	 * @param port port to connect to
	 */
	public FaceRecognitionService(int retries, String ipAddress, int port) throws IOException
	{
		this.takePictureRetries = retries;
		this.url = String.format("http://%s:%d", ipAddress, port);

		if (Files.exists(FACES_FOLDER) && !Files.isDirectory(FACES_FOLDER))
			Files.delete(FACES_FOLDER);

		if (!Files.exists(FACES_FOLDER))
			Files.createDirectory(FACES_FOLDER);
	}

	void trainCurrent(String faceID) throws IOException
	{
		Path facePath = FACES_FOLDER.resolve(faceID+".jpg");
		BufferedImage bufferedImage= new BufferedImage(currentImage.getWidth(null), currentImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		ImageIO.write(bufferedImage, "jpg", facePath.toFile());
		train(faceID, facePath.toFile());
	}

	/**
	 * Train the service to recognize a new face
	 */
	public void train(String faceID, File image) throws IOException
	{
		try (
			FileInputStream fis = new FileInputStream(image);
			CloseableHttpClient httpClient = HttpClientBuilder.create().build()
		)
		{
			HttpEntity httpEntity = MultipartEntityBuilder.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.addTextBody("name", faceID)
				.addTextBody("id", faceID)
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
	public DetectResult detect(InputStream image) throws IOException
	{
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build())
		{
			HttpEntity httpEntity = MultipartEntityBuilder.create()
					.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
					.addPart("file", new InputStreamBody(image, "image"))
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

	/** TEST */
	public static void main(String[] argv) throws IOException
	{
		FaceRecognitionService service = new FaceRecognitionService(10, "10.4.48.239", 8080);
		service.train("tmp", new File("C:\\piproject\\2faces.jpg"));
		DetectResult result = service.detect(new FileInputStream("C:\\piproject\\2faces.jpg"));
		//File image = new File("C:\\piproject\\faces\\image_0001.jpg");
		//File image = new File("C:\\piproject\\2faces.jpg");
		//File image = new File("C:\\piproject\\galaxy.jpg");
		System.out.println("DONE");
	}

	public static class WebResource extends ServerResource
	{
		/** Handles the conversion of objects to/from json. */
		private final Gson gson = new Gson();

		public FaceRecognitionService getFaceRecognitionService()
		{
			return (FaceRecognitionService)getContext().getAttributes().get(IDENTIFIER);
		}

		@Get("json")
		public synchronized JsonRepresentation getImage()
		{
			try
			{
				FaceRecognitionService service = getFaceRecognitionService();
				for (int i = 0; i < service.takePictureRetries; i++)
				{
					Image image = ImageIO.read(service.PI_CAMERA);
					service.currentImage = image;

					BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
					bufferedImage.getGraphics().drawImage(image, 0, 0, null);
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					ImageIO.write(bufferedImage, "jpg", os);
					os.flush();
					byte[] imageBuffer = os.toByteArray();
					os.close();
					InputStream fis = new ByteArrayInputStream(imageBuffer);

					DetectResult detectResult = service.detect(fis);
					if ((detectResult.isSuccess() && detectResult.getFacesCount() == 1) || i == service.takePictureRetries-1)
					{
						// image is sent base64 encoded
						String imageB64 = Base64.getEncoder().encodeToString(imageBuffer);
						return new JsonRepresentation(gson.toJson(detectResult));
//						return new JsonRepresentation(gson.toJson(imageB64));
					}
				}

			}
			catch (IOException e)
			{
				e.printStackTrace();
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}
			return new JsonRepresentation(gson.toJson(""));
		}

		@Post("json")
		public synchronized void setTarget(JsonRepresentation targetJson)
		{
			System.out.println("Set image as target...");
			try
			{
				String faceID = gson.fromJson(targetJson.getText(), String.class);
				FaceRecognitionService service = getFaceRecognitionService();
				service.trainCurrent(faceID);
			}
			catch (IOException e)
			{
				setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			}

		}
	}
}
