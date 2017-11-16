package com.nerf.facerecognition;

import com.google.gson.Gson;

public class DetectResult
{
	private static final Gson GSON = new Gson();

	private boolean success;

	private int facesCount;

	private String error;

	private Face[] faces;

	public static DetectResult valueOf(String json)
	{
		return GSON.fromJson(json, DetectResult.class);
	}

	public static class Face
	{
		private Rectangle rect;

		private boolean matched;

		private String id;

		private String name;
	}

	public static class Rectangle
	{
		private int top;

		private int left;

		private int width;

		private int height;
	}
}
