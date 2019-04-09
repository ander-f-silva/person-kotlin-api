CREATE TABLE email (
    id             VARCHAR(32) PRIMARY KEY,
    person_id      VARCHAR(32),
    address        VARCHAR(100),
    CONSTRAINT fk_person_id_email FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE email_aud (
    id               VARCHAR(32),
    rev              BIGINT,
    revtype          TINYINT,
    person_id        VARCHAR(32),
    person_id_mod    BIT,
    address          VARCHAR(100),
    address_mod      BIT,
    person_mod       BIT
);

