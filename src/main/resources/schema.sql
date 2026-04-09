-- user
CREATE TABLE IF NOT EXISTS `USER`(
    email VARCHAR(40) PRIMARY KEY,
    `name` VARCHAR(40) NOT NULL,
    `login` VARCHAR(40) NOT NULL,
    `password` VARCHAR(40) NOT NULL,
    `address` VARCHAR(255),
    last_modified DATE
);