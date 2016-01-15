package com.dockerapp.dao.entities;


import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@MappedSuperclass
public abstract class InsertableEntity {
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "inserted_ts", nullable = false)
    private DateTime insertedTs;

    @PrePersist
    private void onPrePersistInsertableEntity() {
        insertedTs = DateTime.now();
    }
}
