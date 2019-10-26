package io.jenkins.plugins.globalbuildnotification;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;

import java.util.ArrayList;
import java.util.List;

@Extension
public class NotificationConfiguration extends GlobalConfiguration {

    public static NotificationConfiguration get() {
        return GlobalConfiguration.all().get(NotificationConfiguration.class);
    }

    private List<Endpoint> endpoints;

    public NotificationConfiguration() {
        this.endpoints = new ArrayList<Endpoint>();
        load();
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        save();
    }

}
