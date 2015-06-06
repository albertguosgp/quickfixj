package org.quickfixj.murano.fix;

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
	@Autowired
	private ThreadedSocketAcceptor acceptor;
	@Autowired
	private MessageStoreFactory messageStoreFactory;
	@Autowired
	private SessionSettings sessionSettings;
	@Autowired
	private LogFactory logFactory;
	@Autowired
	private Application application;
	@Autowired
	private MessageFactory messageFactory;

	@PostConstruct
	public void init() throws ConfigError {
		acceptor = new ThreadedSocketAcceptor(application, messageStoreFactory, sessionSettings, logFactory, messageFactory);
	}


	@PreDestroy
	@Override
	public void destroy() {
		acceptor.stop();
	}
}
