package com.zinkworks.services.ndoslackbotpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NDOSlackNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(NDOSlackNotificationApplication.class, args);
	}

}
