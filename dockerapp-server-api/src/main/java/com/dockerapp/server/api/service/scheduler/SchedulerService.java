package com.dockerapp.server.api.service.scheduler;

import org.joda.time.DateTime;

public interface SchedulerService {
    void startJob(JobType jobType, String id, DateTime startAt, DateTime endAtHint);

    void stopJob(JobType jobType, String id);
}
