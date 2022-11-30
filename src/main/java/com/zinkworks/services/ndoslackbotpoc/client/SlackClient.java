package com.zinkworks.services.ndoslackbotpoc.client;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;

public class SlackClient {

    public WebhookResponse sendMessage(final String content, final String webhookUrl) throws IOException {
        final Payload payload = Payload.builder()
                .text(content)
                .build();
        return Slack.getInstance().send(webhookUrl, payload);
    }
}
