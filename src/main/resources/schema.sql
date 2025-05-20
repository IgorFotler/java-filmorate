--DROP TABLE IF EXISTS "LIKES", "FRIENDS", "USERS", "GENRE_ITEMS", "GENRES", "MPA", "FILMS";

CREATE TABLE IF NOT EXISTS MPA
(
    mpa_id      INTEGER PRIMARY KEY,
    name        VARCHAR(25) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(512),
    release_date DATE,
    duration    INTEGER,
    mpa_id      INTEGER,
    FOREIGN KEY (mpa_id) REFERENCES MPA (mpa_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS genres
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(55) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS genre_items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id     INTEGER NOT NULL,
    genre_id    INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE CASCADE,
    UNIQUE (genre_id, film_id)
);

CREATE TABLE IF NOT EXISTS users
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255),
    login       VARCHAR(255) NOT NULL CHECK (LENGTH(login) >= 5),
    email       VARCHAR (255),
    birthday    TIMESTAMP
);

CREATE TABLE IF NOT EXISTS friends
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id     INTEGER NOT NULL,
    friend_id   INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS likes
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id     INTEGER NOT NULL,
    user_id     INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    UNIQUE (film_id, user_id)
);