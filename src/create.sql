CREATE DATABASE scheduler;

CREATE USER 'java'@'localhost' IDENTIFIED BY 'coffee';

GRANT ALL PRIVILEGES ON scheduler.* TO 'java'@'localhost';

USE scheduler;

CREATE TABLE Users (
  user_id  INTEGER     NOT NULL,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(20) NOT NULL,
  email    VARCHAR(40) NOT NULL,
  phone    VARCHAR(20),
  CONSTRAINT users_pk PRIMARY KEY (user_id, username, password)
);

CREATE TABLE Appointments (
  appointment_id INTEGER     NOT NULL,
  user_id        INTEGER     NOT NULL,
  title          VARCHAR(40) NULL,
  start_date     DATETIME    NOT NULL,
  end_date       DATETIME    NOT NULL,
  reminder       INT         NULL,
  CONSTRAINT appointment_pk PRIMARY KEY (appointment_id)
);

ALTER TABLE Appointments
    ADD CONSTRAINT appointment_users_fk FOREIGN KEY (user_id) REFERENCES Users (user_id);

INSERT INTO scheduler.users (user_id, username, password, email, phone)
VALUES (0, 'crisischris', 'cag343', 'cagbrothers@gmail.com', '424-210-1877'),
       (1, 'godiegogo', 'dr343', 'diego@sn.gy', '555-123-4567'),
       (2, 'guest', '', '', NULL);
