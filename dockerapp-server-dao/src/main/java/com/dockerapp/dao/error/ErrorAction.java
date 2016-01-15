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
@Table(name = "error_action")
public final class ErrorAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "error_code")
    private DockerAppError errorCode;

    @Column(name = "order_hint")
    private Integer orderHint;

    @Column(name = "error_action")
    private String errorAction;

    private ErrorAction() {
        // required for JPA
    }

    public String getErrorAction() {
        return errorAction;
    }
}
