# Global Build Notification Plugin

[![Build Status](https://www.travis-ci.org/anxk/global-build-notification-plugin.svg?branch=master)](https://www.travis-ci.org/anxk/global-build-notification-plugin)

This plugin adds the function to send every build event of Jenkins to HTTP Endpoint.

## Installation

Clone this repo on your disk, navigate into folder `global-build-notification-plugin` and run command `mvn verify`, when it completed, upload `target/global-build-notification.hpi` to the update center of Jenkins and install it.

Also you can download `global-build-notification.hpi` from [release page](https://github.com/anxk/global-build-notification-plugin/releases), then upload it to the update center of Jenkins.

### Configuration

Go to manage > configure > Global Build Notification Plugin, add a http endpoint by specifying its URL, Filter (regular expression to restrict the job by full name) and annotation, for example:

<p align="center">
	<img src="images/configuration-example.png" alt="configuration-example.png"  width=100% height=100%>
	<p align="center">
		<em>Configuration Example</em>
	</p>
</p>

If you configure the plugin just as the example above, when a build in Jenkins completed (or started), you will receive messages from the endpoint like this:

```json
{
    "HDuration": "2.7 sec",
    "causes": "Started by user anonymous",
    "duration": 2758,
    "eventId": "f797f1d6-3b30-4c01-80bb-1d7e46ac8f24",
    "jobName": "test",
    "result": "SUCCESS",
    "timestamp": 1572102686886,
    "url": "http://localhost:8080/jenkins/job/test/32/",
    "annotation": {
        "region": "east",
        "project": "test"
    }
}
```
