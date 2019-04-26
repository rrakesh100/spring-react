package com.educative.dao;

import com.educative.jwt.BaseModel;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public abstract  class UpdateableDeletableModel  extends BaseModel {

    @UpdateTimestamp
    @Column(name = "last_updated", nullable = true)
    protected Date lastUpdated;

    @Column(name="deleted")
    protected boolean deleted;

}
