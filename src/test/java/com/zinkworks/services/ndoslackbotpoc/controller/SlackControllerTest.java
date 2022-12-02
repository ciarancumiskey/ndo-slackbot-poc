package com.zinkworks.services.ndoslackbotpoc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slack.api.bolt.response.Response;
import com.zinkworks.services.ndoslackbotpoc.TestConstants;
import com.zinkworks.services.ndoslackbotpoc.TestEnvironment;
import com.zinkworks.services.ndoslackbotpoc.model.SlackNotificationRequest;
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
		final MvcResult helloResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.HELLO_PATH,
						"Tester", this.webhookUrl);
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
		final MvcResult helloResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.HELLO_PATH,
				"Tester", "https://hooks.slack.com/services/this/totally/wontWork");
		Assertions.assertNotNull(helloResult);
		final MockHttpServletResponse response = helloResult.getResponse();
		Assertions.assertEquals(404, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("no_team", slackResponse.getBody());
	}

	@Test
	public void testPostingPlainCardMessage() throws Exception {
		final MvcResult plainCardMessageResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.PLAIN_CARD_PATH,
				"Testing " + ZonedDateTime.now(), this.webhookUrl);
		Assertions.assertNotNull(plainCardMessageResult);
		final MockHttpServletResponse response = plainCardMessageResult.getResponse();
		Assertions.assertEquals(200, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("ok", slackResponse.getBody());
	}

	@Test
	public void testPostingPlainCardMsgToWrongWebhook() throws Exception {
		final MvcResult helloResult = postSlackMessage(TestConstants.SLACK_PATH + TestConstants.PLAIN_CARD_PATH,
				"Testing " + ZonedDateTime.now(), "https://hooks.slack.com/services/this/totally/wontWork");
		Assertions.assertNotNull(helloResult);
		final MockHttpServletResponse response = helloResult.getResponse();
		Assertions.assertEquals(404, response.getStatus());

		final String responseContent = response.getContentAsString();
		final Response slackResponse = g.fromJson(responseContent, Response.class);
		Assertions.assertEquals("no_team", slackResponse.getBody());
	}

	private MvcResult postSlackMessage(final String endpointPath, final String content, final String webhookUrl)
			throws Exception {
		final SlackNotificationRequest request = new SlackNotificationRequest(content, webhookUrl);
		return mockMvc.perform(
						MockMvcRequestBuilders.post(endpointPath)
								.contentType(MediaType.APPLICATION_JSON_VALUE)
								.content(g.toJson(request))
								.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
	}
}
