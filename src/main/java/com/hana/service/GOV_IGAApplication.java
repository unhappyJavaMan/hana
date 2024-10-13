package com.hana.service;

import com.hana.service.Utils.LogUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@PropertySource("${system.properties.path}")
@SpringBootApplication
public class GOV_IGAApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application) {
		return application.sources(GOV_IGAApplication.class);
	}

	public static void main(String[] args) {
		LogUtils.enableSqlLogging();
		SpringApplication.run(GOV_IGAApplication.class, args);
	}
}
