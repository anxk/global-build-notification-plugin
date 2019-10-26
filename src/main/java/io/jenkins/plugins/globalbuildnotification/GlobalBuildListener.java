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
        HttpPublisher.publish(NotificationConfiguration.get().getEndpoints(), new Event(run));
    }

    @Override
    public void onCompleted(Run<?, ?> run, @Nonnull TaskListener listener) {
        HttpPublisher.publish(NotificationConfiguration.get().getEndpoints(), new Event(run));
    }

}

