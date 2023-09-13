INSERT INTO USERS (NAME, EMAIL, PASSWORD, ENABLED, DELETED)
VALUES ('user', 'user@yandex.ru', '{noop}userPassword', TRUE, FALSE),
       ('admin', 'admin@gmail.com', '{noop}adminPassword', TRUE, FALSE),
       ('superAdmin', 'super.admin@gmail.com', '{noop}superAdminPassword', TRUE, FALSE),
       ('banned', 'banned@gmail.com', '{noop}bannedPassword', FALSE, FALSE),
       ('deleted', 'deleted@gmail.com', '{noop}deletedPassword', TRUE, TRUE);

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 'USER'),
       (2, 'USER'),
       (2, 'ADMIN'),
       (3, 'USER'),
       (3, 'ADMIN'),
       (3, 'SUPER_ADMIN'),
       (4, 'USER'),
       (5, 'USER');

INSERT INTO RESTAURANT (NAME, ENABLED, DELETED)
VALUES ('Burger King', TRUE, FALSE),
       ('KFC', TRUE, FALSE),
       ('Unavailable Restaurant', FALSE, FALSE),
       ('Deleted Restaurant', TRUE, TRUE);

INSERT INTO DISH (PRICE, RESTAURANT_ID, NAME, ENABLED, DELETED)
VALUES (150, 1, 'Burger', TRUE, FALSE),
       (250, 1, 'Deleted Dish', TRUE, TRUE),
       (200, 1, 'Cheeseburger', TRUE, FALSE),
       (199, 1, 'Unavailable Dish', FALSE, FALSE),
       (300, 2, 'Bucket', TRUE, FALSE),
       (100, 2, 'Fries', TRUE, FALSE),
       (200, 3, 'Dish from Unavailable', TRUE, FALSE),
       (300, 4, 'Dish from Deleted', TRUE, FALSE);

INSERT INTO LUNCH (DATE, RESTAURANT_ID, ENABLED)
VALUES ('2020-09-01', 1, TRUE),
       ('2020-09-01', 2, FALSE),
       ('2020-09-01', 3, TRUE),
       ('2020-09-01', 4, TRUE);

INSERT INTO LUNCH_ITEM (DISH_ID, LUNCH_ID)
VALUES (1, 1),
       (6, 2),
       (7, 3),
       (8, 4);

INSERT INTO VOTE (DATE, LUNCH_ID, USER_ID)
VALUES ('2020-09-01', 1, 1);