CREATE TABLE `user` (
  `username` VARCHAR(255) PRIMARY KEY,
  `password` VARCHAR(255) NOT NULL,
  `account_non_expired` BIT(1) NOT NULL,
  `account_non_locked` BIT(1) NOT NULL,
  `credentials_non_expired` BIT(1) NOT NULL,
  `enabled` BIT(1) NOT NULL
);

CREATE TABLE `user_role` (
  `roles` VARCHAR(255) DEFAULT NULL,
  `username` VARCHAR(255) NOT NULL,
   FOREIGN KEY (`username`) REFERENCES `user` (`username`)
);

CREATE TABLE `destination` (
  `uuid` UUID PRIMARY KEY,
  `description` VARCHAR(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `location` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  FOREIGN KEY (`username`) REFERENCES `user` (`username`)
);

CREATE TABLE `itinerary` (
  `uuid` UUID PRIMARY KEY,
  `activities` VARCHAR(255) DEFAULT NULL,
  `destination_uuid` UUID NOT NULL,
   FOREIGN KEY (`destination_uuid`) REFERENCES `destination` (`uuid`)
);

CREATE TABLE `expense` (
  `uuid` UUID NOT NULL PRIMARY KEY,
  `description` VARCHAR(255) DEFAULT NULL,
  `category` VARCHAR(255) NOT NULL,
  `amount` DECIMAL(38,2) NOT NULL,
  `itinerary_uuid` UUID NOT NULL,
   FOREIGN KEY (`itinerary_uuid`) REFERENCES `itinerary` (`uuid`)
);