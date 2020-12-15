drop table if exists orders;
drop table if exists rockets;
drop table if exists userroles;
drop table if exists roles;
drop table if exists statuses;
drop table if exists users;

CREATE TABLE `Users` (
                         `id` int PRIMARY KEY AUTO_INCREMENT,
                         `first_name` nvarchar NOT NULL,
                         `last_name` nvarchar NOT NULL,
                         `email` nvarchar UNIQUE NOT NULL,
                         `phone_number` nvarchar NOT NULL,
                         `password` nvarchar NOT NULL,
                         `planet` nvarchar NOT NULL,
                         `country_or_colony` nvarchar NOT NULL,
                         `city_or_district` nvarchar NOT NULL,
                         `street` nvarchar NOT NULL,
                         `number` int NOT NULL
);

CREATE TABLE `UserRoles` (
                             `user_id` int,
                             `role_id` int
);

CREATE TABLE `Roles` (
                         `id` int PRIMARY KEY AUTO_INCREMENT,
                         `name` nvarchar UNIQUE NOT NULL,
                         `rank` int UNIQUE NOT NULL
);

CREATE TABLE `Orders` (
                          `id` int PRIMARY KEY AUTO_INCREMENT,
                          `uuid` uuid default random_uuid(),
                          `user_id` int,
                          `rocket_id` int,
                          `status_id` int,
                          `mass` float NOT NULL,
                          `width` float NOT NULL,
                          `height` float NOT NULL,
                          `depth` float NOT NULL,
                          `cost` float NOT NULL,
                          `planet` nvarchar NOT NULL,
                          `country_or_colony` nvarchar NOT NULL,
                          `city_or_district` nvarchar NOT NULL,
                          `street` nvarchar NOT NULL,
                          `number` int NOT NULL
);

CREATE TABLE `Statuses` (
                            `id` int PRIMARY KEY AUTO_INCREMENT,
                            `status` nvarchar UNIQUE NOT NULL
);

CREATE TABLE `Rockets` (
                           `id` int PRIMARY KEY AUTO_INCREMENT,
                           `name` nvarchar NOT NULL,
                           `depart_location` nvarchar NOT NULL,
                           `departure` datetime NOT NULL,
                           `arrival` datetime NOT NULL,
                           `price_per_kilo` float NOT NULL,
                           `max_mass` float NOT NULL,
                           `max_volume` float NOT NULL,
                           `available_mass` float NOT NULL,
                           `available_volume` float NOT NULL
);

ALTER TABLE `UserRoles` ADD FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`);

ALTER TABLE `UserRoles` ADD FOREIGN KEY (`role_id`) REFERENCES `Roles` (`id`);

ALTER TABLE `Orders` ADD FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`);

ALTER TABLE `Orders` ADD FOREIGN KEY (`rocket_id`) REFERENCES `Rockets` (`id`);

ALTER TABLE `Orders` ADD FOREIGN KEY (`status_id`) REFERENCES `Statuses` (`id`);
