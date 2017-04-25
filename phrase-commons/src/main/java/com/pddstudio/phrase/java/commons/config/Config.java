package com.pddstudio.phrase.java.commons.config;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pddstudio on 17/10/2016.
 */
public final class Config {

	private static Config config;

	private static final Map<String, Object> defaultConfig;

	static {
		//create the fallback default config
		defaultConfig = new HashMap<>();
		//set the default values
		defaultConfig.put("logging.enabled", false);
		defaultConfig.put("build.profile", "???");
	}

	private static Config getConfig() {
		if(config == null) {
			config = new Config();
		}
		return config;
	}

	private final Configuration properties;
	private final boolean initSuccess;

	private Config() {
		Configuration configuration;
		try {
			Configurations configurations = new Configurations();

			configuration = configurations.properties(new File("configuration.properties"));
		} catch (ConfigurationException ce) {
			ce.printStackTrace();
			configuration = null;
		}
		this.properties = configuration;
		this.initSuccess = configuration != null;
	}

	private <T> T getProperty(String propertyName, Class<T> objClass) {
		if(initSuccess) {
			return properties.get(objClass, propertyName);
		} else {
			Object property = defaultConfig.get(propertyName);
			return objClass.cast(property);
		}
	}

	public static boolean loggingEnabled() {
		return getConfig().getProperty("logging.enabled", Boolean.class);
	}

	public static String getBuildProfile() {
		return getConfig().getProperty("build.profile", String.class);
	}

}
