package org.quickfixj.murano.bootstrap;

import quickfix.SessionSettings;

import javax.annotation.PostConstruct;

public interface SessionSettingsLoader {

	@PostConstruct
	public void load();

	public SessionSettings getSessionSettings();
}
