package com.educative.dao;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role extends UpdateableDeletableModel {

    @Getter @Setter
    private String name;

    @Getter @Setter
    @ManyToMany(mappedBy = "roles")
    private Set<Group> groups;


}
