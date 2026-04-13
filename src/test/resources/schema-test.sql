-- user
CREATE TABLE IF NOT EXISTS `USER`(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(40) UNIQUE NOT NULL,
    `name` VARCHAR(40) NOT NULL,
    `login` VARCHAR(40) NOT NULL,
    `password` VARCHAR(40) NOT NULL,
    `address` VARCHAR(255),
    last_modified DATE
);