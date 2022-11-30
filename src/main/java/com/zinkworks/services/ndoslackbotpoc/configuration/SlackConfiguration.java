package com.zinkworks.services.ndoslackbotpoc.configuration;

import com.zinkworks.services.ndoslackbotpoc.client.SlackClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfiguration {

    @Bean
    public SlackClient slackClient(){
        return new SlackClient();
    }
}
