package io.jenkins.plugins.globalbuildnotification;

import javax.annotation.Nonnull;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

@Extension
public class GlobalBuildListener extends RunListener<Run<?, ?>> {

    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        publish(run, Event.STARTED);
    }

    @Override
    public void onCompleted(Run<?, ?> run, @Nonnull TaskListener listener) {
        publish(run, Event.COMPLETED);
    }

    public void publish(Run<?, ?> run, String eventType) {
        HttpPublisher.publish(NotificationConfiguration.get().getEndpoints(), new Event(run, eventType));
    }

}
