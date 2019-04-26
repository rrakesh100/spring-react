package com.educative.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="users")
public class User extends AbstractUser{


    @Getter @Setter
    private String addressLine1;
    @Getter @Setter
    private String addressLine2;
    @Getter @Setter
    private String landMark;
    @Getter @Setter
    private String district;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private City city;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private State state;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private Country country;

    @Getter @Setter
    private String pincode;

    @Getter @Setter
    private String imageUrl; //may be in S3 or local file system or byte[] photo

    @Getter @Setter
    private BigDecimal penalty;

    @Getter @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_groups", joinColumns = {@JoinColumn(name = "user_id")} , inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<Group> groups;


}
