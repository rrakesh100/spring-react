package com.educative.dao;

import javax.persistence.*;

@Entity
@Table(name="book_copies")
public class BookCopy {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; //it's own UUID

    @ManyToOne
    private Book book; //foreign key reference to the "id" in Book. column = book_id

    private int copyNumber; // nth copy of book

    @Enumerated(value = EnumType.STRING)
    private BookStatus status;

    private boolean isReferenceCopy;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="current_owner")
    private User currentOwner; // user_id

}
