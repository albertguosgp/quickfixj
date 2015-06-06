package org.quickfixj.murano.bootstrap.datasource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = "dev")
public class DevDataSourceConfig {

}
