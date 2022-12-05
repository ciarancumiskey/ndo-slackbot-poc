package com.zinkworks.services.ndoslackbotpoc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slack.api.bolt.response.Response;
import com.zinkworks.services.ndoslackbotpoc.TestConstants;
import com.zinkworks.services.ndoslackbotpoc.TestEnvironment;
import com.zinkworks.services.ndoslackbotpoc.model.AlertRequest;
import com.zinkworks.services.ndoslackbotpoc.model.Level;
import com.zinkworks.services.ndoslackbotpoc.model.SlackNotificationRequest;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SlackControllerTest {

	private final Gson g = new GsonBuilder().setPrettyPrinting().create();

	@Autowired private MockMvc mockMvc;

	private String webhookUrl;

	@BeforeAll
	private void setUp() throws Exception {
		TestEnvironment.start();
		this.webhookUrl = System.getenv(TestConstants.ENV_WEBHOOK);
	}

	@Test
	public void testPostingHello() throws Exception {
		final SlackNotificationRequest request = new SlackNotificationRequest("Testing from testPostingHello()", webhookUrl);
		final MvcResult helloResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.HELLO_PATH, request);
		Assertions.assertNotNull(helloResult);
		final MockHttpServletResponse response = helloResult.getResponse();
		Assertions.assertNotNull(response);
		Assertions.assertEquals(200, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("ok", slackResponse.getBody());
	}

	@Test
	public void testPostingHelloToWrongWebhook() throws Exception {
		final SlackNotificationRequest request =
				new SlackNotificationRequest("Testing from testPostingHelloToWrongWebhook()",
						TestConstants.INVALID_WEBHOOK_URL);
		final MvcResult helloResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.HELLO_PATH, request);
		Assertions.assertNotNull(helloResult);
		final MockHttpServletResponse response = helloResult.getResponse();
		Assertions.assertEquals(404, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("no_team", slackResponse.getBody());
	}

	@Test
	public void testPostingPlainCardMessage() throws Exception {
		final SlackNotificationRequest request =
				new SlackNotificationRequest("Testing from testPostingPlainCardMessage()", webhookUrl);
		final MvcResult plainCardMessageResult =
				postSlackMessage(TestConstants.SLACK_PATH + TestConstants.PLAIN_CARD_PATH, request);
		Assertions.assertNotNull(plainCardMessageResult);
		final MockHttpServletResponse response = plainCardMessageResult.getResponse();
		Assertions.assertEquals(200, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("ok", slackResponse.getBody());
	}

	@Test
	public void testPostingPlainCardMsgToWrongWebhook() throws Exception {
		final SlackNotificationRequest request =
				new SlackNotificationRequest("Testing from testPostingPlainCardMsgToWrongWebhook()",
					TestConstants.INVALID_WEBHOOK_URL);
		final MvcResult helloResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.PLAIN_CARD_PATH,
				request);
		Assertions.assertNotNull(helloResult);
		final MockHttpServletResponse response = helloResult.getResponse();
		Assertions.assertEquals(404, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("no_team", slackResponse.getBody());
	}

	@Test
	public void testPostingOperationalAlertMessage() throws Exception {
		final AlertRequest operationalAlertRequest = new AlertRequest(Level.OPERATIONAL, "Testing Operational Message");
		operationalAlertRequest.setWebhookUrl(this.webhookUrl);
		operationalAlertRequest.setContent("Everything's fine, this is just a test.");
		final MvcResult operationalAlertMessageResult =
				postSlackMessage(TestConstants.SLACK_PATH + TestConstants.ALERT_PATH, operationalAlertRequest);
		Assertions.assertNotNull(operationalAlertMessageResult);
		final MockHttpServletResponse operationalResultResponse = operationalAlertMessageResult.getResponse();
		Assertions.assertEquals(200, operationalResultResponse.getStatus());

		final String operationalResponseContent = operationalResultResponse.getContentAsString();
		final Response operationalSlackResponse = g.fromJson(operationalResponseContent, Response.class);
		Assertions.assertEquals("ok", operationalSlackResponse.getBody());
	}

	@Test
	public void testPostingWarningAlertMessage() throws Exception {
		final AlertRequest warningAlertRequest = new AlertRequest(Level.WARN, "Testing Warning Message");
		warningAlertRequest.setWebhookUrl(this.webhookUrl);
		warningAlertRequest.setContent("Don't be alarmed, this is just a test.");
		final MvcResult warningAlertMessageResult =
				postSlackMessage(TestConstants.SLACK_PATH + TestConstants.ALERT_PATH, warningAlertRequest);
		Assertions.assertNotNull(warningAlertMessageResult);
		final MockHttpServletResponse warningResultResponse = warningAlertMessageResult.getResponse();
		Assertions.assertEquals(200, warningResultResponse.getStatus());

		final String warningResultContent = warningResultResponse.getContentAsString();
		final Response warningSlackResponse = g.fromJson(warningResultContent, Response.class);
		Assertions.assertEquals("ok", warningSlackResponse.getBody());
	}

	@Test
	public void testPostingErrorAlertMessage() throws Exception {
		final AlertRequest errorAlertRequest = new AlertRequest(Level.ERROR, "Testing Error Message");
		errorAlertRequest.setWebhookUrl(this.webhookUrl);
		errorAlertRequest.setContent("Test error message sent from testPostingErrorAlertMessage()");
		final MvcResult errorAlertMessageResult =
				postSlackMessage(TestConstants.SLACK_PATH + TestConstants.ALERT_PATH, errorAlertRequest);
		Assertions.assertNotNull(errorAlertMessageResult);
		final MockHttpServletResponse errorResultResponse = errorAlertMessageResult.getResponse();
		Assertions.assertEquals(200, errorResultResponse.getStatus());

		final String errorResultContent = errorResultResponse.getContentAsString();
		final Response errorSlackResponse = g.fromJson(errorResultContent, Response.class);
		Assertions.assertEquals("ok", errorSlackResponse.getBody());
	}

	@Test
	public void testPostingAlertMessageWithoutLevel() throws Exception {
		final AlertRequest missingLevelAlertRequest = new AlertRequest();
		missingLevelAlertRequest.setWebhookUrl(this.webhookUrl);
		missingLevelAlertRequest.setTitle("This alert message is missing a level");
		final MvcResult missingLevelMessageResult = postSlackMessage(
				TestConstants.SLACK_PATH + TestConstants.ALERT_PATH, missingLevelAlertRequest);
		Assertions.assertNotNull(missingLevelMessageResult);
		final MockHttpServletResponse missingLevelResponse = missingLevelMessageResult.getResponse();
		Assertions.assertEquals(HttpStatus.BAD_REQUEST_400, missingLevelResponse.getStatus());
	}

	@Test
	public void testPostingAlertMessageWithoutTitle() throws Exception {
		final AlertRequest missingTitleAlertRequest = new AlertRequest();
		missingTitleAlertRequest.setWebhookUrl(this.webhookUrl);
		missingTitleAlertRequest.setLevel(Level.ERROR);
		final MvcResult missingTitleMessageResult = postSlackMessage(
				TestConstants.SLACK_PATH + TestConstants.ALERT_PATH, missingTitleAlertRequest);
		Assertions.assertNotNull(missingTitleMessageResult);
		final MockHttpServletResponse missingTitleResponse = missingTitleMessageResult.getResponse();
		Assertions.assertEquals(HttpStatus.BAD_REQUEST_400, missingTitleResponse.getStatus());
	}

	@Test
	public void testPostingAlertMessageWithoutWebhook() throws Exception {
		final AlertRequest missingWebhookUrlRequest = new AlertRequest();
		missingWebhookUrlRequest.setTitle("This alert message is missing a webhook URL");
		missingWebhookUrlRequest.setLevel(Level.ERROR);
		MvcResult missingWebhookUrlMessageResult = postSlackMessage(
				TestConstants.SLACK_PATH + TestConstants.ALERT_PATH, missingWebhookUrlRequest);
		Assertions.assertNotNull(missingWebhookUrlMessageResult);
		final MockHttpServletResponse missingWebhookUrlResponse = missingWebhookUrlMessageResult.getResponse();
		Assertions.assertEquals(HttpStatus.BAD_REQUEST_400, missingWebhookUrlResponse.getStatus());
	}

	private MvcResult postSlackMessage(final String endpointPath,
									   final SlackNotificationRequest request) throws Exception {
		return mockMvc.perform(
						MockMvcRequestBuilders.post(endpointPath)
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(g.toJson(request))
								.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
}
