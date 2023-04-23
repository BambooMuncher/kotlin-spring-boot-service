
CREATE TABLE actors(
    id SERIAL PRIMARY KEY,
    name text NOT NULL,
    lowercase_name text NOT NULL,
    created_at timestamp NOT NULL,
    last_updated_at timestamp NOT NULL,
    UNIQUE(lowercase_name)
);

CREATE TABLE performances(
    actor_id int NOT NULL,
    movie_id int NOT NULL,
    PRIMARY KEY(actor_id, movie_id)
);

CREATE TABLE movies(
    id SERIAL PRIMARY KEY,
    title text NOT NULL,
    lowercase_title text NOT NULL,
    release_date date NOT NULL,
    created_at timestamp NOT NULL,
    last_updated_at timestamp NOT NULL,
    UNIQUE(lowercase_title, release_date)
);
