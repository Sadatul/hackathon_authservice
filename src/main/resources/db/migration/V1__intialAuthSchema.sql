CREATE TABLE users
(
    id                       BIGINT AUTO_INCREMENT NOT NULL,
    username                    VARCHAR(255)          NOT NULL,
    password                 VARCHAR(255)          NOT NULL,
    full_name                VARCHAR(255)          NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);