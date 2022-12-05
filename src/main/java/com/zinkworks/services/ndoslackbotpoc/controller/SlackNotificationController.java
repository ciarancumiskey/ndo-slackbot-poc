package com.zinkworks.services.ndoslackbotpoc.controller;

import com.slack.api.bolt.response.Response;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.webhook.WebhookResponse;
import com.zinkworks.services.ndoslackbotpoc.client.SlackClient;
import com.zinkworks.services.ndoslackbotpoc.constants.AppConstants;
import com.zinkworks.services.ndoslackbotpoc.model.AlertRequest;
import com.zinkworks.services.ndoslackbotpoc.model.SlackNotificationRequest;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = AppConstants.SLACK_PATH)
@OpenAPIDefinition(info = @Info(title = "NDO Slackbot POC",
                                version = "0.3.0",
                                description = "NDO Slackbot API"))
@Tag(name = "Slack Notification API")
public class SlackNotificationController {

    @Autowired private SlackClient slackClient;

    @Operation(summary = "Say Hello",
            description = "Posts a greeting for a particular user to the channel corresponding to the webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful post",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorised",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Service error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Response.class))),
    })
    @PostMapping(AppConstants.HELLO_PATH)
    public ResponseEntity<Response> helloSlack(@RequestBody SlackNotificationRequest request)
            throws IOException, MissingRequestValueException {
        WebhookResponse webhookResponse = slackClient.sendMessage("Hello %s".formatted(request.getContent()),
                request.getWebhookUrl(), new ArrayList<>());

        final Response slackResponse = new Response();
        slackResponse.setBody(webhookResponse.getBody());
        slackResponse.setStatusCode(webhookResponse.getCode());
        slackResponse.setHeaders(webhookResponse.getHeaders());
        return new ResponseEntity<>(slackResponse, HttpStatus.valueOf(webhookResponse.getCode()));
    }

    @Operation(summary = "Post plain message",
            description = "Posts a plain message to the channel corresponding to the webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful post",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorised",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Service error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
    })
    @PostMapping(AppConstants.PLAIN_CARD_PATH)
    public ResponseEntity<Response> plainCardSlackMessage(@RequestBody SlackNotificationRequest request)
            throws IOException, MissingRequestValueException {
        final SectionBlock messageBlock = new SectionBlock();
        messageBlock.setText(new MarkdownTextObject(request.getContent(), false));
        //add a divider between this and the next message
        WebhookResponse webhookResponse = slackClient.sendMessage(request.getContent(),
                request.getWebhookUrl(), List.of(messageBlock, new DividerBlock()));

        final Response slackResponse = new Response();
        slackResponse.setBody(webhookResponse.getBody());
        slackResponse.setStatusCode(webhookResponse.getCode());
        slackResponse.setHeaders(webhookResponse.getHeaders());
        return new ResponseEntity<>(slackResponse, HttpStatus.valueOf(webhookResponse.getCode()));
    }

    @Operation(summary = "Post alert message",
            description = "Posts an alert message to the channel corresponding to the webhook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alert successfully posted",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorised",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", description = "Service error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Response.class))),
    })
    @PostMapping(AppConstants.ALERT_PATH)
    public ResponseEntity<Response> postAlertMessage(@RequestBody @Valid AlertRequest request)
            throws IOException, MissingRequestValueException {
        if(request.getLevel() == null || request.getTitle() == null || request.getTitle().isBlank()) {
            final Response missingDataResponse = new Response();
            missingDataResponse.setBody("Please add either a Level or title to your request.");
            return new ResponseEntity<>(missingDataResponse, HttpStatus.BAD_REQUEST);
        }
        final SectionBlock messageLevelAndTitleBlock = new SectionBlock();

        //Add asterisks around the title to make it bold
        String titleText = "\t*" + request.getTitle() + "*";
        titleText = switch(request.getLevel()) {
            case ERROR: yield AppConstants.EMOJI_RED_DOT + titleText;
            case WARN: yield AppConstants.EMOJI_ORANGE_DOT + titleText;
            case OPERATIONAL: yield AppConstants.EMOJI_GREEN_DOT + titleText;
        };

        messageLevelAndTitleBlock.setText(new MarkdownTextObject(titleText, true));

        final SectionBlock messageSummaryBlock = new SectionBlock();
        messageSummaryBlock.setText(new MarkdownTextObject(request.getContent(), true));

        WebhookResponse webhookResponse = slackClient.sendMessage(null,
                request.getWebhookUrl(), List.of(messageLevelAndTitleBlock, messageSummaryBlock));

        final Response slackResponse = new Response();

        slackResponse.setBody(webhookResponse.getBody());
        slackResponse.setStatusCode(webhookResponse.getCode());
        slackResponse.setHeaders(webhookResponse.getHeaders());
        return new ResponseEntity<>(slackResponse, HttpStatus.valueOf(webhookResponse.getCode()));
    }
}
