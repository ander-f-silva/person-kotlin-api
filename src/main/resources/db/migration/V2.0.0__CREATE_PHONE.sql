CREATE TABLE phone (
    id             VARCHAR(32) PRIMARY KEY,
    person_id      VARCHAR(32),
    area_code      NUMERIC(2),
    number         NUMERIC(16) NOT NULL,
    type           VARCHAR(11) NOT NULL,
    CONSTRAINT fk_person_id_phone FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE phone_aud (
    id               VARCHAR(32),
    rev              BIGINT,
    revtype          TINYINT,
    person_id        VARCHAR(32),
    person_id_mod    BIT,
    area_code        NUMERIC(2),
    area_code_mod    BIT,
    number           NUMERIC(16) NOT NULL,
    number_mod       BIT,
    type             VARCHAR(11) NOT NULL,
    type_mod         BIT,
    person_mod       BIT
);

