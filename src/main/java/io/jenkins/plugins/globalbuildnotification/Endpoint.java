package io.jenkins.plugins.globalbuildnotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.common.base.Splitter;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;

public class Endpoint extends AbstractDescribableImpl<Endpoint> {

    private String url;
    private String regex;
    private Map<String, String> annotation;

    @DataBoundConstructor
    public Endpoint(String url, String regex, String annotation) {
        this.url = url;
        this.regex = regex;
        setAnnotation(annotation);
    }

    @DataBoundSetter
    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @DataBoundSetter
    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    @DataBoundSetter
    public void setAnnotation(String annotation) {
        Map<String, String> result = new HashMap<String, String>();
        Iterator<String> iter = Splitter.on(",")
            .omitEmptyStrings()
            .trimResults()
            .split(annotation)
            .iterator();
        while (iter.hasNext()) {
            String str = iter.next();
            List<String> kv = new ArrayList<String>();
            for (String var : str.split("=")) {
                kv.add(var);
            }
            if (kv.size() == 0) {
                continue;
            } else if (kv.size() == 1) {
                kv.add("");
            }
            if (!result.containsKey(kv.get(0))) {
                result.put(kv.get(0), kv.get(1));
            }
        }
        this.annotation = result;
    }

    public String getAnnotation() {
        StringBuilder out = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, String> kv : annotation.entrySet()) {
            count ++;
            out.append(kv.getKey())
                .append("=")
                .append(kv.getValue());
            if (count != annotation.size()) {
                out.append(", ");
            }
        }
        return out.toString();
    }

    public Map<String, String> getRealAnnotation() {
        return annotation;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<Endpoint> {

        public FormValidation doCheckUrl(@QueryParameter String value) {
            if (value.startsWith("http") || value.startsWith("https")) {
                return FormValidation.ok();
            }
            return FormValidation.error("Please specify a valid url");
        }
    
        public FormValidation doCheckRegex(@QueryParameter String value) {
            try {
                Pattern.compile(value);
                return FormValidation.ok();
            } catch (PatternSyntaxException e) {
                return FormValidation.error("Please specify an invalid regular expression");
            }
        }

    }

}