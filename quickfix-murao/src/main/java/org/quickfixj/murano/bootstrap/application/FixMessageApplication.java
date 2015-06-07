package org.quickfixj.murano.bootstrap.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

public class FixMessageApplication extends MessageCracker implements Application {
	private static Logger logger = LoggerFactory.getLogger(FixMessageApplication.class);

	@Override
	public void onCreate(SessionID sessionID) {
		logger.info("Session ID {} has been created", sessionID);
	}

	@Override
	public void onLogon(SessionID sessionID) {
		logger.info("Session ID {} logged on", sessionID);
	}

	@Override
	public void onLogout(SessionID sessionID) {

	}

	@Override
	public void toAdmin(Message message, SessionID sessionID) {

	}

	@Override
	public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

	}

	@Override
	public void toApp(Message message, SessionID sessionID) throws DoNotSend {

	}

	@Override
	public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

	}
}
