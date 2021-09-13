package com.kine.email.utils;

import java.util.Iterator;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class KineRegisterUtils {

	private KineRegisterUtils() {
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static Optional<JSONObject> getKineAPIKeyObj(String input, JSONArray loadResourceFiles) {
		Iterator<?> temp = loadResourceFiles.iterator();
		while (temp.hasNext()) {
			JSONObject obj = (JSONObject) temp.next();
			if (obj.get("code").equals(input) || obj.get("id").equals(input)) {
				return Optional.of(obj);
			}
		}
		return Optional.empty();
	}
}
