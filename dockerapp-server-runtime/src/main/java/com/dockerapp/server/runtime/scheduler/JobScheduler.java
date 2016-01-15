package com.dockerapp.server.runtime.scheduler;

import org.joda.time.DateTime;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public interface JobScheduler {
    void schedule(Scheduler scheduler, String id, DateTime startAt, DateTime endAt) throws SchedulerException;

    void cancel(Scheduler scheduler, String id) throws SchedulerException;

    JobKey getKey(String id);
}
