DROP TABLE IF EXISTS FEATURE_FLAG.FEATURE;
DROP TABLE IF EXISTS FEATURE_FLAG.USER_GROUP;
DROP TABLE IF EXISTS FEATURE_FLAG.USER_FEATURE_MAPPING;

CREATE TABLE FEATURE_FLAG.FEATURE (
                         id               serial       not null primary key,
                         enabled BOOLEAN                      NOT NULL,
                         name VARCHAR(128) UNIQUE             NOT NULL
);

CREATE TABLE FEATURE_FLAG.USER_GROUP (
                            id               serial       not null primary key,
                            user_name VARCHAR(128)                NOT NULL,
                            password VARCHAR(128)                 NOT NULL,
                            first_name VARCHAR(128)               NOT NULL,
                            last_name VARCHAR(128)                NOT NULL,
                            created_at TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE FEATURE_FLAG.USER_GROUP ADD CONSTRAINT UNIQUE_USER_GROUP UNIQUE(first_name, last_name, password);

CREATE TABLE FEATURE_FLAG.USER_FEATURE_MAPPING (
                                      user_id BIGINT  NOT NULL,
                                      feature_id BIGINT NOT NULL,
                                      FOREIGN KEY (user_id) REFERENCES USER_GROUP(id),
                                      FOREIGN KEY (feature_id) REFERENCES FEATURE(id),
                                      PRIMARY KEY (user_id, feature_id)
);

CREATE TABLE FEATURE_FLAG.ROLE (
                                         id               serial       not null primary key,
                                         name VARCHAR(128)                NOT NULL
);

CREATE TABLE FEATURE_FLAG.USER_ROLE_MAPPING (
                                                   user_id BIGINT  NOT NULL,
                                                   role_id BIGINT NOT NULL,
                                                   FOREIGN KEY (user_id) REFERENCES USER_GROUP(id),
                                                   FOREIGN KEY (role_id) REFERENCES ROLE(id),
                                                   PRIMARY KEY (user_id, role_id)
);

CREATE TABLE FEATURE_FLAG.REFRESH_TOKEN (
                                   id               serial       not null primary key,
                                   token VARCHAR(255)                NOT NULL,
                                   created_at TIMESTAMP              DEFAULT CURRENT_TIMESTAMP
);