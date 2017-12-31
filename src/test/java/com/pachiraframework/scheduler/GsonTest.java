package com.pachiraframework.scheduler;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTest {
	public Map<String, Object> getPropertiesMap() {
		Gson gson = new GsonBuilder().create();
		@SuppressWarnings("unchecked")
		Map<String, Object> rs = gson.fromJson(Strings.nullToEmpty(""), Map.class);
		return rs;
	}
	
	@Test
	public void testMap() {
		Map<String, Object> map = getPropertiesMap();
		System.out.println(map);
	}
}
