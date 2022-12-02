package com.zinkworks.services.ndoslackbotpoc.client;

import com.slack.api.Slack;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;

import java.io.IOException;
import java.util.List;

public class SlackClient {

    public WebhookResponse sendMessage(final String content, final String webhookUrl,
                                       final List<LayoutBlock> messageDecorations) throws IOException {
        final Payload payload = Payload.builder()
                .blocks(messageDecorations)
                .text(content)
                .build();
        return Slack.getInstance().send(webhookUrl, payload);
    }
}
