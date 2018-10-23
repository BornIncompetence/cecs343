CREATE DATABASE scheduler;

CREATE TABLE Users (
  user_id  INTEGER     NOT NULL,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  email    VARCHAR(40) NOT NULL,
  phone    VARCHAR(20),
  CONSTRAINT users_pk PRIMARY KEY (user_id)
);

CREATE TABLE Appointments (
  appointment_id INTEGER  NOT NULL,
  user_id        INTEGER  NOT NULL,
  start_date     DATETIME NOT NULL,
  end_date       DATETIME NOT NULL,
  CONSTRAINT appointment_pk PRIMARY KEY (appointment_id)
);

ALTER TABLE Appointments
    ADD CONSTRAINT appointment_users_fk FOREIGN KEY (user_id) REFERENCES Users (user_id);
