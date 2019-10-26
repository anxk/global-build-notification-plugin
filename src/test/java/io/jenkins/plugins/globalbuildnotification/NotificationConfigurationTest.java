package io.jenkins.plugins.globalbuildnotification;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.jvnet.hudson.test.RestartableJenkinsRule;

public class NotificationConfigurationTest {

    @Rule
    public RestartableJenkinsRule rr = new RestartableJenkinsRule();

    /**
     * Tries to exercise enough code paths to catch common mistakes:
     * <ul>
     * <li>missing {@code load}
     * <li>missing {@code save}
     * <li>misnamed or absent getter/setter
     * <li>misnamed {@code textbox}
     * </ul>
     */
    @Test
    public void uiAndStorage() {
        // rr.then(r -> {
        //     // assertArrayEquals("not set initially", new ArrayList<Endpoint>(), NotificationConfiguration.get().getEndpoints());
        //     HtmlForm config = r.createWebClient().goTo("configure").getFormByName("io-jenkins-plugins-globalbuildnotification")
        //     HtmlTextInput textbox1 = config.getInputByName("url");
        //     textbox1.setText("http://localhost:9999/notification");
        //     HtmlTextInput textbox2 = config.getInputByName("regex");
        //     textbox2.setText("^testJob$");
        //     HtmlTextInput textbox3 = config.getInputByName("tag");
        //     textbox3.setText("testEndpoint");
        //     r.submit(config);
        //     assertEquals("global config page let us edit it", "http://localhost:9999/notification", NotificationConfiguration.get().getEndpoints().get(0).getUrl());
        //     assertEquals("global config page let us edit it", "^testJob$", NotificationConfiguration.get().getEndpoints().get(0).getRegex());
        //     assertEquals("global config page let us edit it", "testEndpoint", NotificationConfiguration.get().getEndpoints().get(0).getTag());
        // });
        // rr.then(r -> {
        //     assertEquals("global config page let us edit it", "http://localhost:9999/notification", NotificationConfiguration.get().getEndpoints().get(0).getUrl());
        //     assertEquals("global config page let us edit it", "^testJob$", NotificationConfiguration.get().getEndpoints().get(0).getRegex());
        //     assertEquals("global config page let us edit it", "testEndpoint", NotificationConfiguration.get().getEndpoints().get(0).getTag());
        // });
    }

}
