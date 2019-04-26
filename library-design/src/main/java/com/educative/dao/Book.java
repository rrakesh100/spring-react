package com.educative.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String author1;
    @Getter @Setter
    private String author2;
    @Getter @Setter
    private String author3;

    @Getter @Setter
    private String publisher;

    @Getter @Setter
    private String isbn;

    @Getter @Setter
    private int copies;

    @Getter @Setter
    private String rackNumber;

    @Getter @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name= "book_id")
    private List<BookCopy> bookCopies;

}
