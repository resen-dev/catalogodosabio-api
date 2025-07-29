CREATE TABLE books (
    id UUID PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    author_id UUID NOT NULL,
    genre_id UUID NOT NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES author(id),
    CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genre(id)
);
