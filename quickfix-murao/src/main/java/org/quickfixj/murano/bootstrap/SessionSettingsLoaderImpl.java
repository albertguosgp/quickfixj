package org.quickfixj.murano.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.ConfigError;
import quickfix.SessionSettings;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SessionSettingsLoaderImpl implements SessionSettingsLoader {
	private static Logger logger = LoggerFactory.getLogger(SessionSettingsLoaderImpl.class);
	private SessionSettings sessionSettings;

	@PostConstruct
	@Override
	public void load() {
		logger.info("Start loading Fix session settings...");
		try {
			getSettingsInputStream();
		} catch (FileNotFoundException e) {
			logger.error("Failed to find executor.cfg in classpath. System exit", e);
		} catch (ConfigError configError) {
			logger.error("Load Fix session settings fail. System exit", configError);
		}
	}

	@Override
	public SessionSettings getSessionSettings() {
		return sessionSettings;
	}

	private void getSettingsInputStream() throws FileNotFoundException, ConfigError {
		InputStream inputStream = SessionSettingsLoaderImpl.class.getClassLoader().getResourceAsStream("executor.cfg");
		sessionSettings = new SessionSettings(inputStream);
	}
}
