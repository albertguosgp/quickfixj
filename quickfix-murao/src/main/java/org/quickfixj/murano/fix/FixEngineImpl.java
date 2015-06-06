package org.quickfixj.murano.fix;

import org.springframework.beans.factory.annotation.Autowired;
import quickfix.Acceptor;
import quickfix.MessageStoreFactory;

public class FixEngineImpl implements FixEngine {

	@Autowired
	private MessageStoreFactory messageStoreFactory;

	private Acceptor acceptor;

	@Override
	public void destroy() {
		acceptor.stop();
	}
}
