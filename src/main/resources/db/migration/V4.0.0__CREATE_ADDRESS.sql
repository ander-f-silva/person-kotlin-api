CREATE TABLE address (
    id             VARCHAR(32) PRIMARY KEY,
    person_id      VARCHAR(32),
    public_place   VARCHAR(80) NOT NULL,
    number         NUMERIC(8)  NOT NULL,
    complement     VARCHAR(50) NOT NULL,
    neighborhood   VARCHAR(20) NOT NULL,
    city           VARCHAR(20) NOT NULL,
    state          VARCHAR(2)  NOT NULL,
    CONSTRAINT fk_person_id_address FOREIGN KEY (person_id) REFERENCES person(id)
);

CREATE TABLE address_aud (
    id               VARCHAR(32),
    rev              BIGINT,
    revtype          TINYINT,
    person_id        VARCHAR(32),
    person_id_mod    BIT,
    public_place     VARCHAR(80) NOT NULL,
    public_place_mod BIT,
    number           NUMERIC(8)  NOT NULL,
    number_mod       BIT,
    complement       VARCHAR(50) NOT NULL,
    complement_mod   BIT,
    neighborhood     VARCHAR(20) NOT NULL,
    neighborhood_mod BIT,
    city             VARCHAR(20) NOT NULL,
    city_mod         BIT,
    state            VARCHAR(2)  NOT NULL,
    state_mod        BIT,
    person_mod       BIT
);
