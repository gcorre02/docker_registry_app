package com.dockerapp.server.runtime.scheduler.jobs;

import com.dockerapp.server.api.RequestContextHolder;
import com.dockerapp.server.api.security.AuthIdentity;
import com.dockerapp.server.api.security.Role;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AutomationJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutomationJob.class);

    @Override
    public final void execute(JobExecutionContext context) throws JobExecutionException {
        RequestContextHolder.get().setIdentity(new AuthIdentity("AUTOMATION", Role.AUTOMATION));
        try {
            executeWithIdentity(context);
        } catch (JobExecutionException | RuntimeException e) {
            LOGGER.error(
                    String.format("error executing scheduled job=%s, error=%s", context.getJobDetail().getKey(), e.getMessage()),
                    e);
            throw e;
        } finally {
            RequestContextHolder.clear();
        }
    }

    protected abstract void executeWithIdentity(JobExecutionContext context) throws JobExecutionException;
}
