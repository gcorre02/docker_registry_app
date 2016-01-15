package com.dockerapp.server.runtime.scheduler;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.error.DockerAppException;
import com.dockerapp.server.api.service.scheduler.JobType;
import com.dockerapp.server.api.service.scheduler.SchedulerService;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SchedulerServiceImpl implements SchedulerService {

    private static final Map<JobType, ? extends JobScheduler> JOB_SCHEDULER_MAP = ImmutableMap.of();

    @Autowired
    private Scheduler scheduler;

    @Override
    public void startJob(JobType jobType, String id, DateTime startAt, DateTime endAtHint) {
        try {
            JOB_SCHEDULER_MAP.get(jobType).schedule(scheduler, id, MoreObjects.firstNonNull(startAt, DateTime.now()), endAtHint);
        } catch (SchedulerException e) {
            throw new DockerAppException(DockerAppError.SCHEDULER_ERROR, e);
        }
    }

    @Override
    public void stopJob(JobType jobType, String id) {
        try {
            JOB_SCHEDULER_MAP.get(jobType).cancel(scheduler, id);
        } catch (SchedulerException e) {
            throw new DockerAppException(DockerAppError.SCHEDULER_ERROR, e);
        }
    }
}
