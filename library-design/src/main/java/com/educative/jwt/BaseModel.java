package com.educative.jwt;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Getter @Setter
    protected Long id;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    @Getter @Setter
    protected Date created;


}
