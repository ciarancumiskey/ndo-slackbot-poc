package com.zinkworks.services.ndoslackbotpoc.controller;

import com.slack.api.Slack;
import com.slack.api.bolt.response.Response;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import com.zinkworks.services.ndoslackbotpoc.model.SlackNotificationRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/slack")
public class SlackNotificationController {

    @PostMapping("/hello")
    public Response helloSlack(@RequestBody SlackNotificationRequest request) throws IOException {

        //TODO: extract this out into a "Client" class
        Payload payload = Payload.builder()
                .channel(request.getChannel())
                .text("Hello %s".formatted(request.getContent()))
                .build();
        WebhookResponse webhookResponse = Slack.getInstance().send(request.getWebhookUrl(), payload);

        final Response slackResponse = new Response();
        slackResponse.setStatusCode(webhookResponse.getCode());
        slackResponse.setBody(webhookResponse.getBody());
        return slackResponse;
    }
}
