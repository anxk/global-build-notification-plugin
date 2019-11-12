package io.jenkins.plugins.globalbuildnotification;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import jenkins.model.GlobalConfiguration;

public class NotificationConfigurationTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void uiAndStorage() {
        rr.then(r -> {
            List<Endpoint> endpointsInit = NotificationConfiguration.get().getEndpoints();

            assertEquals("endpoints should be empty initially", new ArrayList<Endpoint>(), endpointsInit);

            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            config.getElementsByAttribute("button", "id", "yui-gen5-button").get(0).click();
            HtmlTextInput url = config.getInputByName("_.url");
            HtmlTextInput regex = config.getInputByName("_.regex");
            HtmlTextInput annotation = config.getInputByName("_.annotation");
            url.setText("http://localhost:9999/notification");
            regex.setText("^testJob$");
            annotation.setText("annotation1=test");
            r.submit(config);

            List<Endpoint> endpoints = NotificationConfiguration.get().getEndpoints();

            assertEquals("The url should be http://localhost:9999/notification", "http://localhost:9999/notification", endpoints.get(0).getUrl());
            assertEquals("The regex should be ^testJob$", "^testJob$", endpoints.get(0).getRegex());
            assertEquals("The annonation should be annotation1=test", "annotation1=test", endpoints.get(0).getAnnotation());
        });
        rr.then(r -> {
            List<Endpoint> endpoints = NotificationConfiguration.get().getEndpoints();

            assertEquals("Still there after restart of Jenkins", "http://localhost:9999/notification", endpoints.get(0).getUrl());
            assertEquals("Still there after restart of Jenkins", "^testJob$", endpoints.get(0).getRegex());
            assertEquals("Still there after restart of Jenkins", "annotation1=test", endpoints.get(0).getAnnotation());
        });
    }

    @Test
    public void annonationTest() {
        List<Endpoint> endpoints = j.getInstance()
            .getExtensionList(GlobalConfiguration.class)
            .get(NotificationConfiguration.class).getEndpoints();
            
        endpoints.add(new Endpoint("", "", ""));
        endpoints.get(0).setAnnotation("  ");
        assertEquals("Should be empty", "", endpoints.get(0).getAnnotation());
        endpoints.get(0).setAnnotation(" = ");
        assertEquals("Should be empty", "", endpoints.get(0).getAnnotation());
        endpoints.get(0).setAnnotation(" =, ");
        assertEquals("Should be empty", "", endpoints.get(0).getAnnotation());
        endpoints.get(0).setAnnotation("test=, ");
        assertEquals("Should be test=", "test=", endpoints.get(0).getAnnotation());
        endpoints.get(0).setAnnotation("test=value,  ");
        assertEquals("Should be test=value", "test=value", endpoints.get(0).getAnnotation());
    }

}
