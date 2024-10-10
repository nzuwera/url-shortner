CREATE TABLE short_urls
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    url              VARCHAR(255)          NOT NULL,
    url_id           VARCHAR(255)          NOT NULL,
    expire_timestamp datetime              NULL,
    CONSTRAINT pk_short_urls PRIMARY KEY (id)
);

ALTER TABLE short_urls
    ADD CONSTRAINT uc_short_urls_url UNIQUE (url);

CREATE UNIQUE INDEX URL_ID_IDX ON short_urls (url_id);