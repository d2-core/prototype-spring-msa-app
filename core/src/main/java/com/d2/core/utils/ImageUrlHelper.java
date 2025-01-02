package com.d2.core.utils;

public class ImageUrlHelper {
	public static String objectStorePrefix = "d2-core-creation-storage";

	public static String extractSubstring(String url) {
		int index = url.indexOf(objectStorePrefix);
		if (index != -1) {
			return url.substring(index);
		}
		throw new IllegalArgumentException("Keyword not found in URL: " + objectStorePrefix);
	}
}
