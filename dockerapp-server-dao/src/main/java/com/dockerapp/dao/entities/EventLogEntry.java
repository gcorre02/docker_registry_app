package com.dockerapp.dao.entities;

import com.dockerapp.dao.error.DockerAppError;
import com.dockerapp.dao.event.EventType;
import com.dockerapp.dao.event.EventColumn;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "event_log")
public final class EventLogEntry {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLogEntry.class);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "transaction_id", length = 48, nullable = false)
    private String transactionId;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "event_ts", nullable = false)
    private DateTime eventTs;

    @Column(name = "event_rt", nullable = false)
    private int eventRuntime;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(name = "event_parent_id")
    private Long eventParentId;

    @Column(name = "performed_by_id", length = 48)
    private String performedById;

    @Column(name = "performed_by_role", length = 32)
    private String performedByRole;

    @Column(name = "performed_on_id", length = 48)
    private String performedOnId;

    @Column(name = "remote_ip_address", length = 32)
    private String remoteIpAddress;

    @Column(name = "success")
    private Boolean success;

    @Enumerated(EnumType.STRING)
    @Column(name = "failure_code")
    private DockerAppError failureCode;

    @Column(name = "failure_reason", length = 250)
    private String failureReason;

    @Column(name = "failure_type", length = 250)
    private String failureType;

    @Column(name = "batch_size")
    private Integer batchSize;

    @Column(name = "exclude_existing_when_creating")
    private Boolean excludeExistingWhenCreating;

    public Long getId() {
        return id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public DateTime getEventTs() {
        return eventTs;
    }

    public void setEventTs(DateTime eventTs) {
        this.eventTs = eventTs;
    }

    public int getEventRuntime() {
        return eventRuntime;
    }

    public void setEventRuntime(int eventRuntime) {
        this.eventRuntime = eventRuntime;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public long getEventParentId() {
        return eventParentId;
    }

    public void setEventParentId(long eventParentId) {
        this.eventParentId = eventParentId;
    }

    public String getPerformedById() {
        return performedById;
    }

    public void setPerformedById(String performedById) {
        this.performedById = performedById;
    }

    public String getPerformedByRole() {
        return performedByRole;
    }

    public void setPerformedByRole(String performedByRole) {
        this.performedByRole = performedByRole;
    }

    public String getPerformedOnId() {
        return performedOnId;
    }

    public void setPerformedOnId(String performedOnId) {
        this.performedOnId = performedOnId;
    }

    public String getRemoteIpAddress() {
        return remoteIpAddress;
    }

    public void setRemoteIpAddress(String remoteIpAddress) {
        this.remoteIpAddress = remoteIpAddress;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public DockerAppError getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(DockerAppError failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = truncate("failureReason", failureReason, 250);
    }

    public String getFailureType() {
        return failureType;
    }

    public void setFailureType(String failureType) {
        this.failureType = truncate("failureType", failureType, 250);
    }

    public Integer getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
    }


    public Boolean getExcludeExistingWhenCreating() {
        return excludeExistingWhenCreating;
    }

    public void setExcludeExistingWhenCreating(Boolean excludeExistingWhenCreating) {
        this.excludeExistingWhenCreating = excludeExistingWhenCreating;
    }

    private String truncate(String fieldName, String value, int length) {
        if (value != null && value.length() > length) {
            LOGGER.warn("truncated log entry field [{}] to [{}] characters, value=[{}]", fieldName, length, value);
            return value.substring(0, length);
        }

        return value;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put(EventColumn.TRANSACTION_ID.name(), transactionId);
        map.put(EventColumn.EVENT_TS.name(), eventTs.toString(ISODateTimeFormat.dateTime()));
        map.put(EventColumn.EVENT_RT.name(), String.valueOf(eventRuntime));
        map.put(EventColumn.EVENT_TYPE.name(), eventType.name());
        map.put(EventColumn.EVENT_PARENT_ID.name(), eventParentId == null ? null : String.valueOf(eventParentId));
        map.put(EventColumn.PERFORMED_BY_ID.name(), performedById);
        map.put(EventColumn.PERFORMED_BY_ROLE.name(), performedByRole);
        map.put(EventColumn.PERFORMED_ON_ID.name(), performedOnId);
        map.put(EventColumn.REMOTE_IP_ADDRESS.name(), remoteIpAddress);
        map.put(EventColumn.SUCCESS.name(), success.toString());
        map.put(EventColumn.FAILURE_CODE.name(), failureCode == null ? null : failureCode.name());
        map.put(EventColumn.FAILURE_REASON.name(), failureReason);
        map.put(EventColumn.FAILURE_TYPE.name(), failureType);
        map.put(EventColumn.BATCH_SIZE.name(), batchSize == null ? null : batchSize.toString());
        map.put(EventColumn.EXCLUDE_EXISTING_WHEN_CREATING.name(), excludeExistingWhenCreating == null ? null : excludeExistingWhenCreating.toString());
        return Maps.filterValues(map, Predicates.notNull());
    }
}
