package com.zinkworks.services.ndoslackbotpoc.configuration;

import com.slack.api.bolt.AppConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfiguration {

    @Bean
    public AppConfig slackAppConfig(@Value("${slack.signing.secret}") String signingSecret,
                                    @Value("${slack.bot.token}") String botToken){
        final AppConfig appConfig = new AppConfig();
        appConfig.setSigningSecret(signingSecret);
        appConfig.setSingleTeamBotToken(botToken);
        return appConfig;
    }
}
