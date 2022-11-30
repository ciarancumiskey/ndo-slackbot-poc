# NDO Slackbot Proof of Concept

This is a Slackbot with a Controller which exposes an endpoint for posting messages to a Slack channel.

## How to run
0. [Create a new app](https://api.slack.com/apps/) from scratch for your Slack workspace. 
1. Navigate to *"Incoming Webhooks"*, switch the "Activate" toggle at the top of the page to on, and then click *"Add New Webhook to Workspace"* at the bottom of the page. Select a channel for this Slackbot to post to.
2. Copy the provided webhook URL to use in requests.
3. Run `mvn clean install` to generate a JAR.
4. Run `mvn spring-boot:run -X` to start the app.
5. Send requests to *http://localhost:3000/slack/hello* like so:

  ```
  curl --location --request POST 'http://localhost:3000/slack/hello' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "content": "tester",
      "channel": "ndo-notifications",
      "webhookUrl": "$YOUR_WEBHOOK_URL"
  }'
  ```

or if you're using Powershell:

  ```
  $headers = New-Object "System.Collections.Generic.Dictionary[[String],[String]]"
  $headers.Add("Content-Type", "application/json")

  $body = "{
  `n    `"content`": `"tester`",
  `n    `"channel`": `"ndo-notifications`",
  `n    `"webhookUrl`": `"$YOUR_WEBHOOK_URL`"
  `n}"

  $response = Invoke-RestMethod 'http://localhost:3000/slack/hello' -Method 'POST' -Headers $headers -Body $body
  $response | ConvertTo-Json
  ```

