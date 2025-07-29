package com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class BookRecentlyViewedId implements Serializable {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "book_id")
    private UUID bookId;

    public BookRecentlyViewedId() {
    }

    public BookRecentlyViewedId(UUID userId, UUID bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookRecentlyViewedId that)) return false;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, bookId);
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
}
