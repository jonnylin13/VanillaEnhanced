package com.github.jonnylin13.ve.objects.generic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public abstract class JSONObject {

	public static <T> T fromJson(String jsonString, Class<T> classOfT) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.fromJson(jsonString, classOfT);
	}
	
	public String toJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
	public String toString() {
		return toJson();
	}

}
