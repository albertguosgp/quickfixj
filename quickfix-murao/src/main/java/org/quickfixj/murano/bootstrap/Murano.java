package org.quickfixj.murano.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Murano{
	public static void main(String... args) {
		SpringApplication.run(MuranoAppConfig.class, args);
	}
}
