package com.zinkworks.services.ndoslackbotpoc;

import com.slack.api.bolt.App;
import com.slack.api.bolt.jetty.SlackAppServer;
public class NDOSlackNotificationApplication {

	public static void main(String[] args) throws Exception {
		App app = new App();
		SlackAppServer server = new SlackAppServer(app);
		server.start();
	}

}
