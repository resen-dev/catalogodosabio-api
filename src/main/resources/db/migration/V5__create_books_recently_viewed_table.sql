CREATE TABLE books_recently_viewed (
    user_id UUID NOT NULL,
    book_id UUID NOT NULL,
    last_access TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
