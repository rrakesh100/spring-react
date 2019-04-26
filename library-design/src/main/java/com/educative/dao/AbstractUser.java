package com.educative.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@ToString
public abstract  class AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Getter @Setter
    private String firstName;

    @Getter @Setter
    private String lastName;

    @Getter @Setter
    private int copiesBorrowed;

    @Getter @Setter
    private String password;

//    @Getter @Setter
//    private Date passwordExpiryDate;
//

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currentOwner")
    private List<BookCopy> borrowedBooks;

    @Getter @Setter
    private int maxCopies;

    @Getter @Setter
    private long mobileNumber;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private boolean isAdmin;

}
