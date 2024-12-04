CREATE TABLE `USERS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255),
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp,
  `updated_at` timestamp,
  `role` varchar(255)
);

CREATE TABLE `RENTALS` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` numeric,
  `price` numeric,
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` integer NOT NULL,
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE TABLE `MESSAGES` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `rental_id` integer,
  `user_id` integer,
  `message` varchar(2000),
  `created_at` timestamp,
  `updated_at` timestamp
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);

ALTER TABLE `RENTALS` ADD FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`);

ALTER TABLE `MESSAGES` ADD FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`);

ALTER TABLE `MESSAGES` ADD FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`);


INSERT INTO `USERS` (`email`, `name`, `password`, `created_at`, `updated_at`, `role`)
VALUES 
  ('admin@test.com', 'Admin TEST', '$2a$10$yh/P1pJaW5Rca7l/8Zxjx.QRpmPgQt68eom1qtIgB9.zdsdaKArtS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ROLE_ADMIN'),
  ('test@test.com', 'Test TEST', '$2a$10$ZVzzAxdDMFyDYNZOc1enX.Jk/D2KEvvOdRTsH46ZezJydPYVIsJsK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'ROLE_USER');


INSERT INTO `RENTALS` (`name`, `surface`, `price`, `picture`, `description`, `owner_id`, `created_at`, `updated_at`)
VALUES 
  ('dream house', 432, 300, 'http://localhost:3001/images/rental_01.jpg', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('test house 1', 154, 200, 'http://localhost:3001/images/rental_02.jpg', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
  ('test house 2', 324, 100, 'http://localhost:3001/images/rental_03.jpg', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO `MESSAGES` (`rental_id`, `user_id`, `message`, `created_at`, `updated_at`)
VALUES 
  (1, 2, 'this is a message', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


