package com.educative.jwt;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Table(name = "keys")
public class Key extends BaseModel {

    @Column(name = "issuer", nullable = false)
    @Getter @Setter
    private String issuer;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @Getter @Setter
    private String content;

    @Column(name = "expires_at")
    @Getter @Setter
    private Date expiresAt;

    @Column(name = "description")
    @Getter @Setter
    private String description;

    /**
     * privateKey field is set when creating new record and never saved to DB or any storage.
     */
    @Transient
    @Getter @Setter
    private String privateKey;


}
