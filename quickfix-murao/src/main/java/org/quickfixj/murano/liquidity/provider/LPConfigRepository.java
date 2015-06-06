package org.quickfixj.murano.liquidity.provider;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface LPConfigRepository extends MongoRepository<LPConfig, UUID> {
	public List<LPConfig> findBylpName(String lpName);
}
