package com.dockerapp.dao.event;

public enum EventColumn {
    TRANSACTION_ID,

    EVENT_RT,
    EVENT_TS,
    EVENT_TYPE,
    EVENT_PARENT_ID,

    PERFORMED_BY_ID,
    PERFORMED_BY_ROLE,
    PERFORMED_ON_ID,
    REMOTE_IP_ADDRESS,

    SUCCESS,
    FAILURE_CODE,
    FAILURE_REASON,
    FAILURE_TYPE,

    BATCH_SIZE,
    EXCLUDE_EXISTING_WHEN_CREATING,

    APP_SPECIFIC_EVENT_STUB
}
