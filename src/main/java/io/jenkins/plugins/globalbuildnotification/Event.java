package io.jenkins.plugins.globalbuildnotification;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import hudson.model.Cause;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.model.Jenkins;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Event {

    private static final Logger LOGGER = Logger.getLogger(Event.class.getName());
    
    private String eventId;
    private Long timestamp;
    private String url;
    private String causes;
    private Long duration;
    private String hDuration;
    private String result;
    private String jobName;
    private Map<String, String> envVars;

    public Event(Run<?, ?> run, TaskListener listener) {
        setEventId();
        setTimestamp();
        setUrl(run);
        setCauses(run);
        setJobName(run);
        setDuration(run);
        setHDuration(run);
        setResult(run);
        setEnvVars(run, listener);
    }

    public void setEventId() {
        this.eventId = UUID.randomUUID().toString();
    }

    public String getEventId() {
        return eventId;
    }

    public void setTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setJobName(Run<?, ?> run) {
        this.jobName = run.getParent().getFullName();
    }

    public String getJobName() {
        return jobName;
    }

    public void setCauses(Run<?, ?> run) {
        StringBuilder causesBuilder = new StringBuilder();
        List<Cause> causes = run.getCauses();
        for (int i = 0; i < causes.size(); i++) {
            if (i != 0) {
                causesBuilder.append(", ");
            }
            causesBuilder.append(causes.get(i).getShortDescription());
        }
        this.causes = causesBuilder.toString();
    }

    public String getCauses() {
        return causes;
    }

    public void setUrl(Run<?, ?> run) {
        StringBuilder url = new StringBuilder();
        try {
            String rootUrl = Jenkins.get().getRootUrl();
            if (rootUrl != null) {
                url.append(rootUrl);
            } else {
                LOGGER.log(Level.WARNING , "Maybe jenkins root url was not configured");
            }
            url.append(run.getUrl());
        } catch (IllegalStateException e) {
            LOGGER.log(Level.WARNING , "", e);
        }
        this.url = url.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setDuration(Run<?, ?> run) {
        this.duration = run.getDuration();
    }

    public Long getDuration() {
        return duration;
    }

    public void setHDuration(Run<?, ?> run) {
        this.hDuration = run.getDurationString();
    }

    public String getHDuration() {
        return hDuration;
    }

    public void setResult(Run<?, ?> run) {
        Result result = run.getResult();
        if (result != null) {
            this.result = result.toString();
        }
    }

    public String getResult() {
        return result;
    }

    public void setEnvVars(Run<?, ?> run, @Nonnull TaskListener listener) {
        this.envVars = new TreeMap<String, String>();
        try {
            this.envVars = run.getEnvironment(listener);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Cannot obtain environment variables", e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.WARNING, "Cannot obtain environment variables", e);
        }
    }

    public void resetEnvVars() {
        this.envVars = null;
    }

    public Map<String, String> getEnvVars() {
        return envVars;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json = JSONObject.fromObject(this);
        } catch (JSONException e) {
            LOGGER.log(Level.WARNING , "Fail to covert build " + url + " to proper JSONObject");
        }
        if (json.get("envVars") == null) {
            json.remove("envVars");
        }
        return json;
    }

    public JSONObject mergeFromEndpoint(Endpoint endpoint) {
        JSONObject in = toJson();
        in.put("annotation", endpoint.getRealAnnotation());
        return in;
    }

}