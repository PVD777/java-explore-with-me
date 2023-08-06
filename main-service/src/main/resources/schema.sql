DROP TABLE IF EXISTS compilations_events, compilations, requests, events, categories, users;

CREATE TABLE IF NOT EXISTS users (
                                     id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY ,
                                     name VARCHAR(256) NOT NULL,
                                     email VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
                                          id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY ,
                                          name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
                                      id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY ,
                                      title VARCHAR(128) NOT NULL,
                                      description TEXT NULL,
                                      annotation TEXT NOT NULL,
                                      confirmed_request int,
                                      views int,
                                      category_id INT NOT NULL REFERENCES categories (id),
                                      event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                      published_on TIMESTAMP WITHOUT TIME ZONE,
                                      created_on TIMESTAMP WITHOUT TIME ZONE,
                                      latitude REAL NOT NULL,
                                      longitude REAL NOT NULL,
                                      initiator_id INT NOT NULL REFERENCES users(id),
                                      paid BOOLEAN NOT NULL,
                                      participant_limit INT NOT NULL,
                                      request_moderation BOOLEAN NOT NULL,
                                      state VARCHAR(64)
);


CREATE TABLE IF NOT EXISTS requests (
                                        id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
                                        event_id BIGINT NOT NULL REFERENCES events(id),
                                        requester_id BIGINT NOT NULL REFERENCES users(id),
                                        created TIMESTAMP WITHOUT TIME ZONE,
                                        status VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS compilations (
                                            id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY ,
                                            pinned BOOLEAN,
                                            title VARCHAR(64)
);

CREATE TABLE IF NOT EXISTS compilations_events (
                                                   id INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                                   compilation_id BIGINT NOT NULL REFERENCES compilations (id),
                                                   event_id BIGINT NOT NULL REFERENCES events (id),
                                                   PRIMARY KEY (compilation_id, event_id)
);