package com.dockerapp.dao.error;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "error_cause")
public final class ErrorCause {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_code")
    private DockerAppError errorCode;

    @Column(name = "order_hint")
    private Integer orderHint;

    @Column(name = "error_cause")
    private String errorCause;

    private ErrorCause() {
        // required for JPA
    }

    public String getErrorCause() {
        return errorCause;
    }
}
