package org.quickfixj.murano.fix;

import org.quickfixj.murano.liquidity.provider.LPConfig;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FixEngineManagerImpl implements FixEngineManager {

	private CopyOnWriteArrayList<FixEngine> fixEngines;

	@Override
	public void addFixEngine(FixEngine fixEngine) {
		fixEngines.add(fixEngine);
	}

	@Override
	public boolean removeFixEngine(FixEngine fixEngine) {
		fixEngine.destroy();
		return fixEngines.remove(fixEngine);
	}

	@Override
	public void initFixEngineManagers(List<LPConfig> lpConfigs) {
		fixEngines = new CopyOnWriteArrayList<FixEngine>();

	}
}
