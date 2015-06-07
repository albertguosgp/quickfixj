package org.quickfixj.murano.fix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.ThreadedSocketAcceptor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class FixEngineImpl implements FixEngine {
	private static Logger logger = LoggerFactory.getLogger(FixEngineImpl.class);

	@Autowired private MessageStoreFactory messageStoreFactory;
	@Autowired private SessionSettings sessionSettings;
	@Autowired private LogFactory logFactory;
	@Autowired private Application application;
	@Autowired private MessageFactory messageFactory;

	private ThreadedSocketAcceptor acceptor;

	@PostConstruct
	public void init() throws ConfigError {
		acceptor = new ThreadedSocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
	}

	@PreDestroy
	@Override
	public void destroy() {
		logger.info("Fix engine shutting down...");
		acceptor.stop();
	}
}
