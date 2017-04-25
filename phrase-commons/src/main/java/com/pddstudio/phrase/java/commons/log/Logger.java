package com.pddstudio.phrase.java.commons.log;

import com.pddstudio.phrase.java.commons.config.Config;

/**
 * Created by pddstudio on 16/10/2016.
 */
public class Logger {

	private static final Object LOCK = new Object();

	public static Logger getLogger() {
		return new Logger(null);
	}

	public static Logger getLogger(CharSequence tagPrefix) {
		return new Logger(tagPrefix);
	}

	public static Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getSimpleName());
	}

	private final CharSequence tag;
	private final boolean enabled;

	private Logger(CharSequence tagPrefix) {
		if(tagPrefix == null || tagPrefix.length() == 0) {
			this.tag = null;
		} else {
			this.tag = tagPrefix;
		}
		this.enabled = Config.loggingEnabled();
		System.out.println("Logger initialized! Status: [buildProfile=" + Config.getBuildProfile() + "][enabled=" + enabled + "]");
	}

	private void logMsg(String message) {
		synchronized (LOCK) {
			if(enabled) {
				System.out.println(message);
			}
		}
	}

	private String addPrefix(String toMessage) {
		if(tag != null) {
			toMessage = "[" + tag + "] " + toMessage;
		}
		return toMessage;
	}

	public void log(String message, Object... args) {
		String msg = String.format(message, args);
		logMsg(addPrefix(msg));
	}

}
