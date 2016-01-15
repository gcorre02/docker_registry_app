create table event_log (
    id bigint primary key auto_increment,

    transaction_id varchar(48) not null,

    event_ts timestamp not null,
    event_rt int not null,
    event_type varchar(32) not null,
    event_parent_id bigint,

    performed_by_id varchar(48),
    performed_by_role varchar(32),
    performed_on_id varchar(48),
    remote_ip_address varchar(32),

    success int(1),
    failure_code varchar(64),
    failure_reason varchar(250),
    failure_type varchar(250),

    batch_size int,
    exclude_existing_when_creating int(1)
);

create index idx_event_log_0 on event_log(transaction_id);
create index idx_event_log_1 on event_log(event_type);
create index idx_event_log_2 on event_log(event_ts);
create index idx_event_log_3 on event_log(performed_by_id);