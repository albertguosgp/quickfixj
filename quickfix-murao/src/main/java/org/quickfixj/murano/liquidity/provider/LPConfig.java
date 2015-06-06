package org.quickfixj.murano.liquidity.provider;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class LPConfig {
	@Id
	private UUID id;
	private String lpName;
	private String hostname;
	private int port;

	@Override
	public String toString() {
		return "LPConfig{" +
				"lpName='" + lpName + '\'' +
				", hostname='" + hostname + '\'' +
				", port=" + port +
				'}';
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getLpName() {
		return lpName;
	}

	public void setLpName(String lpName) {
		this.lpName = lpName;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
