CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE collection (
    uuid UUID PRIMARY KEY NOT NULL,
    id BIGINT,
    name TEXT,
    poster_path TEXT,
    backdrop_path TEXT
);

CREATE TABLE company (
    uuid UUID PRIMARY KEY NOT NULL,
    id BIGINT,
    name TEXT,
    UNIQUE (id, name)
);

CREATE TABLE country (
    uuid UUID PRIMARY KEY NOT NULL,
    iso TEXT,
    name TEXT,
    UNIQUE (iso, name)
);

CREATE TABLE genre (
    uuid UUID PRIMARY KEY NOT NULL,
    id BIGINT,
    name TEXT,
    UNIQUE (id, name)
);

CREATE TABLE language (
    uuid UUID PRIMARY KEY NOT NULL,
    iso TEXT,
    name TEXT,
    UNIQUE (iso, name)
);

CREATE TABLE movie (
    uuid UUID PRIMARY KEY NOT NULL,
    id BIGINT,
    adult BOOLEAN,
    imdb_id TEXT,
    budget BIGINT,
    collection UUID REFERENCES collection(uuid),
    homepage TEXT,
    original_language TEXT,
    original_title TEXT,
    poster_path TEXT,
    overview TEXT,
    popularity FLOAT,
    release_date DATE,
    revenue BIGINT,
    runtime FLOAT,
    status TEXT,
    tagline TEXT,
    title TEXT,
    video BOOLEAN,
    vote_average FLOAT,
    vote_count BIGINT
);

CREATE TABLE movie_company (
    movie_uuid   UUID NOT NULL REFERENCES movie (uuid),
    company_uuid UUID NOT NULL REFERENCES company (uuid)
);

CREATE TABLE movie_country (
    movie_uuid   UUID NOT NULL REFERENCES movie (uuid),
    country_uuid UUID NOT NULL REFERENCES country (uuid)
);

CREATE TABLE movie_language (
    movie_uuid   UUID NOT NULL REFERENCES movie (uuid),
    language_uuid UUID NOT NULL REFERENCES language (uuid)
);

CREATE TABLE movie_genre (
    movie_uuid   UUID NOT NULL REFERENCES movie (uuid),
    genre_uuid UUID NOT NULL REFERENCES genre (uuid)
);