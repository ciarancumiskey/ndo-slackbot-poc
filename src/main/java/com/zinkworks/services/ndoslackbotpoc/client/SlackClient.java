package com.zinkworks.services.ndoslackbotpoc.client;

import com.slack.api.Slack;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import org.springframework.web.bind.MissingRequestValueException;

import java.io.IOException;
import java.util.List;

public class SlackClient {

    public WebhookResponse sendMessage(final String content, final String webhookUrl,
                                       final List<LayoutBlock> messageDecorations)
            throws IOException, MissingRequestValueException {
        if(webhookUrl == null || webhookUrl.isBlank()) {
            throw new MissingRequestValueException("Webhook URL is missing from request!");
        }
        final Payload payload = Payload.builder()
                .blocks(messageDecorations)
                .text(content)
                .build();
        return Slack.getInstance().send(webhookUrl, payload);
    }
}
