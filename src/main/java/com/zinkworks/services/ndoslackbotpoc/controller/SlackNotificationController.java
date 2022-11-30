package com.zinkworks.services.ndoslackbotpoc.controller;

import com.slack.api.Slack;
import com.slack.api.bolt.response.Response;
import com.slack.api.webhook.Payload;
import com.slack.api.webhook.WebhookResponse;
import com.zinkworks.services.ndoslackbotpoc.client.SlackClient;
import com.zinkworks.services.ndoslackbotpoc.constants.AppConstants;
import com.zinkworks.services.ndoslackbotpoc.model.SlackNotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(AppConstants.SLACK_PATH)
public class SlackNotificationController {

    @Autowired private SlackClient slackClient;

    @PostMapping(AppConstants.HELLO_PATH)
    public Response helloSlack(@RequestBody SlackNotificationRequest request) throws IOException {

        WebhookResponse webhookResponse = slackClient.sendMessage("Hello %s".formatted(request.getContent()),
                request.getWebhookUrl());

        final Response slackResponse = new Response();
        slackResponse.setStatusCode(webhookResponse.getCode());
        slackResponse.setBody(webhookResponse.getBody());
        return slackResponse;
    }
}
