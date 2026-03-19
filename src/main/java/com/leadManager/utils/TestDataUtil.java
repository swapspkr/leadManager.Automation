package com.leadManager.utils;

import java.util.Random;

public class TestDataUtil {

	public static String getName() {
		return "John_" + new Random().nextInt(1000);
	}

	public static String getEmail() {
		return "john" + new Random().nextInt(1000) + "@test.com";
	}
}
