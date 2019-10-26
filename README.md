# global-build-notification-plugin

This plugin adds the function to send every build event in Jenkins to HTTP Endpoint.

## Configure

configuration example:
<p align="center">
	<img src="images/global-build-notification.png" alt="global-build-notification.png"  width=90% height=90%>
	<p align="center">
		<em>example</em>
	</p>
</p>

message format:
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
        "region": "Shenzhen",
        "project": "Extraordinary Projects"
    }
}
```