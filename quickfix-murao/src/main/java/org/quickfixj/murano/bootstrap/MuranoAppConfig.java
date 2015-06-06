package org.quickfixj.murano.bootstrap;

import com.google.common.collect.Lists;
import org.quickfixj.murano.fix.FixEngine;
import org.quickfixj.murano.fix.FixEngineImpl;
import org.quickfixj.murano.fix.FixEngineManager;
import org.quickfixj.murano.fix.FixEngineManagerImpl;
import org.quickfixj.murano.liquidity.provider.LPConfig;
import org.quickfixj.murano.liquidity.provider.LPConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import quickfix.Application;
import quickfix.CachedFileStoreFactory;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;

import java.util.List;

@Configuration
@EnableAutoConfiguration
@EnableMongoRepositories(basePackages = {"org.quickfixj.murano.liquidity.provider"})
@ComponentScan(basePackages = {"org.quickfixj.murano.bootstrap.datasource"})
public class MuranoAppConfig {
	@Autowired
	private LPConfigRepository lpConfigRepository;

	@Bean
	public FixEngineManager fixEngineManager() {
		List<LPConfig> lps = lpConfigRepository.findAll();
		FixEngineManager fixEngineManager = new FixEngineManagerImpl();
		fixEngineManager.initFixEngineManagers(lps);
		return fixEngineManager;
	}

	@Bean(name = "fixMessageStoreFactory")
	@DependsOn(value = {"sessionSettings"})
	public MessageStoreFactory fixMessageStoreFactory(SessionSettings sessionSettings) {
		return new CachedFileStoreFactory(sessionSettings);
	}

	@Bean(name = "sessionSettingsLoader")
	public SessionSettingsLoader sessionSettingsLoader(){
		return new SessionSettingsLoaderImpl();
	}

	@Bean(name = "sessionSettings")
	@DependsOn(value = "sessionSettingsLoader")
	public SessionSettings sessionSettings(SessionSettingsLoader sessionSettingsLoader) {
		return sessionSettingsLoader.getSessionSettings();
	}

	@Bean
	@DependsOn(value = {"fixMessageStoreFactory", "sessionSettings"})
	public FixEngine fixEngines(MessageStoreFactory messageStoreFactory, SessionSettings sessionSettings, LogFactory logFactory, Application application, MessageFactory messageFactory) throws ConfigError {
		return new FixEngineImpl();
	}

	@Bean
	public MessageFactory messageFactory() {
		return new DefaultMessageFactory();
	}

	@Bean
	public quickfix.LogFactory logFactory(SessionSettings sessionSettings) {
		return new FileLogFactory(sessionSettings);
	}

}
