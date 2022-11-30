package com.zinkworks.services.ndoslackbotpoc.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SlackControllerTest {

	private final Gson g = new GsonBuilder().setPrettyPrinting().create();

	@Autowired private MockMvc mockMvc;

	@BeforeAll
	private void setUp() throws Exception {
		TestEnvironment.start();
	}

	@Test
	public void testPostingHello() throws Exception {
		final SlackNotificationRequest slackNotificationRequest = new SlackNotificationRequest();
		slackNotificationRequest.setContent("Tester");
		slackNotificationRequest.setWebhookUrl("FIXME"); //fixme Replace this with your webhook URL

		final MvcResult helloResult = mockMvc.perform(
				MockMvcRequestBuilders.post(TestConstants.SLACK_PATH + TestConstants.HELLO_PATH)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(g.toJson(slackNotificationRequest))
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		Assertions.assertNotNull(helloResult);
		final MockHttpServletResponse response = helloResult.getResponse();
		Assertions.assertNotNull(response);
		Assertions.assertEquals(200, response.getStatus());
	}
}
