CREATE TABLE MEMBERSHIP_TYPES
(
    ID          INTEGER AUTO_INCREMENT PRIMARY KEY,
    NAME        VARCHAR(255)   NOT NULL UNIQUE,
    DESCRIPTION VARCHAR(500),
    PRICE       DECIMAL(10, 2) NOT NULL,
    DURATION    INTEGER        NOT NULL
);

CREATE TABLE MEMBERSHIPS
(
    ID                 INTEGER AUTO_INCREMENT PRIMARY KEY,
    USER_ID            INTEGER   NOT NULL,
    MEMBERSHIP_TYPE_ID INTEGER   NOT NULL,
    START_DATE         TIMESTAMP NOT NULL,
    END_DATE           TIMESTAMP NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
    FOREIGN KEY (MEMBERSHIP_TYPE_ID) REFERENCES MEMBERSHIP_TYPES (ID)
);

CREATE TABLE GROUP_CLASSES
(
    ID               INTEGER AUTO_INCREMENT PRIMARY KEY,
    NAME             VARCHAR(255) NOT NULL,
    DESCRIPTION      VARCHAR(500),
    TYPE             VARCHAR(100) NOT NULL, -- e.g., Yoga, Zumba, etc.
    DATE_TIME        TIMESTAMP    NOT NULL,
    DURATION         INTEGER      NOT NULL, -- in minutes
    MAX_PARTICIPANTS INTEGER      NOT NULL DEFAULT 0
);

CREATE TABLE CLASS_INSTRUCTORS
(
    CLASS_ID   INTEGER NOT NULL,
    INSTRUCTOR_ID INTEGER NOT NULL,
    PRIMARY KEY (CLASS_ID, INSTRUCTOR_ID),
    FOREIGN KEY (CLASS_ID) REFERENCES GROUP_CLASSES (ID) ON DELETE CASCADE,
    FOREIGN KEY (INSTRUCTOR_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE CLASS_PARTICIPANTS
(
    CLASS_ID   INTEGER NOT NULL,
    USER_ID    INTEGER NOT NULL,
    PRIMARY KEY (CLASS_ID, USER_ID),
    FOREIGN KEY (CLASS_ID) REFERENCES GROUP_CLASSES (ID) ON DELETE CASCADE,
    FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

INSERT INTO MEMBERSHIP_TYPES (NAME, DESCRIPTION, PRICE, DURATION)
VALUES ('Mjesečna', 'Pristup teretani na mjesec danas', 50.00, 30),
       ('Kvartalna', 'Pristup teretani na tri mjeseca', 135.00, 90),
       ('Godišnja', 'Pristup teretani na godinu dana', 480.00, 365);

