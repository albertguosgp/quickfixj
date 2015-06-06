package org.quickfixj.murano.fix;

import org.quickfixj.murano.liquidity.provider.LPConfig;

import java.util.List;

public interface FixEngineManager {
	public void addFixEngine(FixEngine fixEngine);

	public boolean removeFixEngine(FixEngine fixEngine);

	public void initFixEngineManagers(List<LPConfig> lpConfigs);
}
