package com.github.jonnylin13.ve.tools;

import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Json {
	
	public static String toJson(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}
	
	public static void toJson(Object obj, Writer writer) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		gson.toJson(obj, writer);
	}
	
	public static <T> T fromJson(String jsonString, Class<T> classOfT) {
		Gson gson = new Gson();
		return gson.fromJson(jsonString, classOfT);
	}

}
