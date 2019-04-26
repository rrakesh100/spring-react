package com.educative.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @Column(name = "user_id")
    @Getter @Setter
    private Long userId;

    @Column(nullable = false)
    @Getter @Setter
    private int attempts;

    @Column(nullable = false)
    @Getter @Setter
    private Date lastUpdated;

    public int increaseAttempt() {
        return ++this.attempts;
    }


}


