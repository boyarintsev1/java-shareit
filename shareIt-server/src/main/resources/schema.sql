DROP TABLE if exists comments;
DROP TABLE if exists requests;
DROP TABLE if exists bookings;
DROP TABLE if exists items;
DROP TABLE if exists users;

CREATE TABLE IF NOT EXISTS users (                 -- создание таблицы пользователей users
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(30) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (                 -- создание таблицы вещей items
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(30) NOT NULL,
  description VARCHAR(200) NOT NULL,
  available BOOL NOT NULL,
  owner_id BIGINT NOT NULL,
  request_id BIGINT,
  CONSTRAINT pk_item PRIMARY KEY (id),
  CONSTRAINT unique_item UNIQUE (name, description, owner_id),
  CONSTRAINT items_users_fk FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (                            -- создание таблицы бронирований bookings
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date timestamp,
  end_date timestamp,
  item_id BIGINT NOT NULL,
  booker_id BIGINT NOT NULL,
  status VARCHAR(200) NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id),
  CONSTRAINT unique_booking UNIQUE (start_date, end_date, item_id),
  CONSTRAINT bookings_items_fk FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT bookings_users_fk FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (                            -- создание таблицы запросов на вещи requests
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(200) NOT NULL,
  requestor_id BIGINT NOT NULL,
  created timestamp NOT NULL,
  CONSTRAINT pk_request PRIMARY KEY (id),
  CONSTRAINT requests_users_fk FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT UQ_REQUEST_DESCRIPTION UNIQUE (description)
);

CREATE TABLE IF NOT EXISTS comments (                            -- создание таблицы отзывов comments
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(1000) NOT NULL,
  item_id BIGINT NOT NULL,
  author_id BIGINT NOT NULL,
  created timestamp NOT NULL,
  CONSTRAINT pk_comment PRIMARY KEY (id),
  CONSTRAINT comments_items_fk FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT comments_users_fk FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);