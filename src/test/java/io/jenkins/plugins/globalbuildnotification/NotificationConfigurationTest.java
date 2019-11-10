package io.jenkins.plugins.globalbuildnotification;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RestartableJenkinsRule;

public class NotificationConfigurationTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void uiAndStorage() {
        rr.then(r -> {
            assertEquals("not set initially", new ArrayList<Endpoint>(), NotificationConfiguration.get().getEndpoints());
            HtmlForm config = r.createWebClient().goTo("configure").getFormByName("config");
            config.getElementsByAttribute("button", "id", "yui-gen5-button").get(0).click();
            HtmlTextInput url = config.getInputByName("_.url");
            HtmlTextInput regex = config.getInputByName("_.regex");
            HtmlTextInput annotation = config.getInputByName("_.annotation");
            HtmlCheckBoxInput withEnvVars = config.getInputByName("_.withEnvVars");
            url.setText("http://localhost:9999/notification");
            regex.setText("^testJob$");
            annotation.setText("annotation1=test");
            withEnvVars.setChecked(true);
            r.submit(config);
            assertEquals("The url should be http://localhost:9999/notification", "http://localhost:9999/notification", NotificationConfiguration.get().getEndpoints().get(0).getUrl());
            assertEquals("The regex should be ^testJob$", "^testJob$", NotificationConfiguration.get().getEndpoints().get(0).getRegex());
            assertEquals("The annonation should be annotation1=test", "annotation1=test", NotificationConfiguration.get().getEndpoints().get(0).getAnnotation());
            assertEquals("The annonation should be true", true, NotificationConfiguration.get().getEndpoints().get(0).getWithEnvVars());
        });
        rr.then(r -> {
            assertEquals("Still there after restart of Jenkins", "http://localhost:9999/notification", NotificationConfiguration.get().getEndpoints().get(0).getUrl());
            assertEquals("Still there after restart of Jenkins", "^testJob$", NotificationConfiguration.get().getEndpoints().get(0).getRegex());
            assertEquals("Still there after restart of Jenkins", "annotation1=test", NotificationConfiguration.get().getEndpoints().get(0).getAnnotation());
            assertEquals("Still there after restart of Jenkins", true, NotificationConfiguration.get().getEndpoints().get(0).getWithEnvVars());
        });
    }

}
