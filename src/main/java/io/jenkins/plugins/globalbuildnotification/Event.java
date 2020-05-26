package io.jenkins.plugins.globalbuildnotification;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.model.Cause;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Result;
import hudson.model.Run;
import jenkins.model.Jenkins;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class Event {

    private static final Logger LOGGER = Logger.getLogger(Event.class.getName());

    protected static final String STARTED = "started";
    protected static final String COMPLETED = "completed";
    
    private String eventId;
    private String eventType;
    private Long timestamp;
    private String url;
    private String causes;
    private Long duration;
    private String result;
    private String jobName;
    private Map<String, String> parameters;

    public Event(Run<?, ?> run, String eventType) {
        setEventId();
        setEventType(eventType);
        setTimestamp(eventType, run);
        setUrl(run);
        setCauses(run);
        setJobName(run);
        setDuration(run);
        setResult(run);
        setParameters(run);
    }

    public void setEventId() {
        this.eventId = UUID.randomUUID().toString();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setTimestamp(String eventType, Run<?, ?> run) {
        if (eventType.equals(STARTED)) {
            this.timestamp = run.getStartTimeInMillis();
        } else {
            this.timestamp = System.currentTimeMillis();
        }
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

    public void setResult(Run<?, ?> run) {
        Result result = run.getResult();
        if (result != null) {
            this.result = result.toString();
        }
    }

    public String getResult() {
        return result;
    }

    public void setParameters(Run<?, ?> run) {
        ParametersAction parametersAction = run.getAction(ParametersAction.class);
        if (parametersAction != null) {
            Map<String, String> _parameters = new TreeMap<String, String>();
            for (ParameterValue p: parametersAction.getAllParameters()) {
                Object value = p.getValue();
                if (value != null) {
                    _parameters.put(p.getName(), value.toString());
                }
            }
            this.parameters = _parameters;
        }
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json = JSONObject.fromObject(this);
        } catch (JSONException e) {
            LOGGER.log(Level.WARNING , "Fail to covert build " + url + " to proper JSONObject");
        }
        return json;
    }

    public JSONObject mergeFromEndpoint(Endpoint endpoint) {
        JSONObject merged = toJson();
        merged.put("annotation", endpoint.getRealAnnotation());
        return merged;
    }

}