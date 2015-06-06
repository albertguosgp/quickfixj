package org.quickfixj.murano.bootstrap.datasource;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(value = {"uat"})
public class UatDataSourceConfig {
}
