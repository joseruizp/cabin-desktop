package com.cabin.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	
	private static final String DEFAULT_PATH = "C:/cabinas/";
	private Properties prop;

	public PropertiesLoader(String file) {
		prop = new Properties();

		try {
			InputStream inputStream = new FileInputStream(new File(DEFAULT_PATH + file));
			prop.load(inputStream);
			System.out.println(prop);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getValue(String key) {
		return prop.getProperty(key);
	}
	
	public Long getLong(String key) {
		return Long.valueOf(prop.getProperty(key));
	}
}
