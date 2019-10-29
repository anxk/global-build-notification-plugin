package io.jenkins.plugins.globalbuildnotification;


import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class HttpPublisher {

    private static final Logger LOGGER = Logger.getLogger(HttpPublisher.class.getName());

    public static void publish(List<Endpoint> endpoints, Event event) {
        for (Endpoint endpoint : endpoints) {
            if (isRegexMatch(endpoint.getRegex(), event.getJobName())) {
                if(!endpoint.getWithEnvVars()) {
                    event.resetEnvVars();
                }
                _publish(endpoint, event);
            }
        }
    }
    
    public static void _publish(Endpoint endpoint, Event event) {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(5000)
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .build();

        try {
            HttpPost request = new HttpPost(endpoint.getUrl());
            request.setConfig(requestConfig);
            request.setHeader("Content-type", "application/json");
            StringEntity requestEntity = new StringEntity(event.mergeFromEndpoint(endpoint).toString(), "UTF-8");
            request.setEntity(requestEntity);
            CloseableHttpResponse response = httpclient.execute(request);
            LOGGER.log(Level.INFO, "Send notification to " + endpoint.getUrl() + " " + response.getStatusLine());
        } catch (UnsupportedCharsetException e) {
            LOGGER.log(Level.WARNING, "Unsupported charset", e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Invalid URL or make JSON text failed", e);
        } catch (ClientProtocolException e) {
            LOGGER.log(Level.WARNING, "Protocol error", e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Connection was aborted", e);
        }
    }

    public static Boolean isRegexMatch(String regex, String value) {
        if (regex.trim().equals("")) {
            return true;
        }
        return Pattern.matches(regex, value);
    }

}
