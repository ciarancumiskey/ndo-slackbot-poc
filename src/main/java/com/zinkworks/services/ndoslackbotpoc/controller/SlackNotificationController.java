package com.zinkworks.services.ndoslackbotpoc.controller;

import com.slack.api.bolt.App;
import com.slack.api.bolt.servlet.SlackAppServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet("/slack/events")
public class SlackNotificationController extends SlackAppServlet {

    public SlackNotificationController(App app) {
        super(app);
    }
}
