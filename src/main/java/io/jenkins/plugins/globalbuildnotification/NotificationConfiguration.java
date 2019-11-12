package io.jenkins.plugins.globalbuildnotification;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.StaplerRequest;

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

    @Override
    public boolean configure(final StaplerRequest req, final JSONObject formData) {
        setEndpoints(req.bindJSONToList(Endpoint.class, formData.get("endpoints")));
        return false;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        save();
    }

}
