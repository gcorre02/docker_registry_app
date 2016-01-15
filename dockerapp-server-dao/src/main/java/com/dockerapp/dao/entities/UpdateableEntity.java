package com.dockerapp.dao.entities;


import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@MappedSuperclass
public abstract class UpdateableEntity extends InsertableEntity {
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "modified_ts", nullable = false)
    private DateTime modifiedTs;

    @PrePersist
    private void onPrePersistUpdateableEntity() {
        modifiedTs = DateTime.now();
    }

    @PreUpdate
    private void onPreUpdateUpdateableEntity() {
        modifiedTs = DateTime.now();
    }
}
