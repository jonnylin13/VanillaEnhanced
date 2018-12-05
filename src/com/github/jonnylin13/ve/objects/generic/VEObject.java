package com.github.jonnylin13.ve.objects.generic;

import com.github.jonnylin13.ve.tools.Json;


public class VEObject {

	public static <T> T fromJson(String jsonString, Class<T> classOfT) {
		return Json.fromJson(jsonString, classOfT);
	}
	
	public String toJson() {
		return Json.toJson(this);
	}
	
	public String toString() {
		return toJson();
	}

}
