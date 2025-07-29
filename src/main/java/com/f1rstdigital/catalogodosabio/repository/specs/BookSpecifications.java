package com.f1rstdigital.catalogodosabio.repository.specs;

import com.f1rstdigital.catalogodosabio.domain.book.Book;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;
import java.util.UUID;

public class BookSpecifications {

    public static Specification<Book> hasAuthorId(UUID authorId) {
        Objects.requireNonNull(authorId, "authorId must not be null");

        return (root, query, builder) ->
                builder.equal(root.get("author").get("id"), authorId);
    }

    public static Specification<Book> hasGenreId(UUID genreId) {
        Objects.requireNonNull(genreId, "genreId must not be null");

        return (root, query, builder) ->
                builder.equal(root.get("genre").get("id"), genreId);
    }

    public static Specification<Book> hasBookId(UUID bookId) {
        Objects.requireNonNull(bookId, "bookId must not be null");

        return (root, query, builder) ->
                builder.equal(root.get("id"), bookId);
    }
}
