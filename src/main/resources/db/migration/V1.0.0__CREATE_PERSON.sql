CREATE TABLE person (
    id             VARCHAR(32) PRIMARY KEY,
    name           VARCHAR(20) NOT NULL,
    last_name      VARCHAR(50) NOT NULL,
    document       VARCHAR(16) NOT NULL,
    birth_date     TIMESTAMP   NOT NULL,
    active         VARCHAR(1),
    created_date   TIMESTAMP   NOT NULL,
    updated_date   TIMESTAMP   NULL
);

CREATE TABLE person_aud (
    id                  VARCHAR(32),
    rev                 BIGINT,
    revtype             TINYINT,
    name                VARCHAR(20) NOT NULL,
    name_mod            BIT,
    last_name           VARCHAR(50) NOT NULL,
    last_name_mod       BIT,
    document            VARCHAR(16) NOT NULL,
    document_mod        BIT,
    birth_date          TIMESTAMP   NOT NULL,
    birth_date_mod      BIT,
    active              VARCHAR(1),
    active_mod          BIT,
    created_date        TIMESTAMP   NOT NULL,
    created_date_mod    BIT,
    updated_date        TIMESTAMP   NULL,
    updated_date_mod    BIT,
    emails_mod          BIT,
    phones_mod          BIT,
    address_mod         BIT
);

create index person_index_name      ON person(name)      USING HASH;
create index person_index_document  ON person(document)  USING HASH;
create index person_index_last_name ON person(last_name) USING HASH;

