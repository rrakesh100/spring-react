package com.educative.dao;

import com.educative.jwt.BaseModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="groups")
public class Group extends UpdateableDeletableModel {

   @Getter @Setter
   private  String name;

   @Getter @Setter
   private String description;


   @ManyToMany(mappedBy = "groups")
   private Set<User> users;

    @Getter @Setter
    @ManyToMany
   @JoinTable(name="group_roles", joinColumns = {@JoinColumn(name = "group_id")} , inverseJoinColumns = {@JoinColumn(name = "role_id")})
   private Set<Role> roles;


}
