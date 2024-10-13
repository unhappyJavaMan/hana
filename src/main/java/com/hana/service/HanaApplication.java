package com.hana.service;

import com.hana.service.Utils.LogUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@PropertySource("${system.properties.path}")
@SpringBootApplication
public class HanaApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(HanaApplication.class);
	}

	public static void main(String[] args) {
		LogUtils.enableSqlLogging();
		SpringApplication.run(HanaApplication.class, args);
	}
}
