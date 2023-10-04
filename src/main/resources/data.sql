INSERT INTO USERS (NAME, EMAIL, PASSWORD, ROLE, ENABLED, DELETED)
VALUES ('user', 'user@yandex.ru', '{noop}userPassword', 'USER', TRUE, FALSE),
       ('admin', 'admin@gmail.com', '{noop}adminPassword', 'ADMIN', TRUE, FALSE),
       ('superAdmin', 'super.admin@gmail.com', '{noop}superAdminPassword', 'SUPER_ADMIN', TRUE, FALSE),
       ('banned', 'banned@gmail.com', '{noop}bannedPassword', 'USER', FALSE, FALSE),
       ('deleted', 'deleted@gmail.com', '{noop}deletedPassword', 'USER', TRUE, TRUE);

INSERT INTO RESTAURANT (NAME, ENABLED, DELETED)
VALUES ('Deleted Restaurant', TRUE, TRUE),
       ('Unavailable Restaurant', FALSE, FALSE),
       ('Burger King', TRUE, FALSE),
       ('KFC', TRUE, FALSE);

INSERT INTO DISH (PRICE, RESTAURANT_ID, NAME, ENABLED, DELETED)
VALUES (150, 3, 'Burger', TRUE, FALSE),
       (250, 3, 'Deleted Dish', TRUE, TRUE),
       (200, 3, 'Cheeseburger', TRUE, FALSE),
       (199, 3, 'Unavailable Dish', FALSE, FALSE),
       (300, 4, 'Bucket', TRUE, FALSE),
       (100, 4, 'Fries', TRUE, FALSE),
       (200, 2, 'Dish from Unavailable', TRUE, FALSE),
       (300, 1, 'Dish from Deleted', TRUE, FALSE);

INSERT INTO LUNCH (DATE, RESTAURANT_ID, ENABLED)
VALUES ('2023-09-01', 3, TRUE),
       ('2023-09-01', 4, FALSE),
       ('2023-09-01', 2, TRUE),
       ('2023-09-01', 1, TRUE);

INSERT INTO LUNCH_ITEM (LUNCH_ID, DISH_ID)
VALUES (1, 1),
       (2, 6),
       (3, 7),
       (4, 8);

INSERT INTO VOTE (DATE, LUNCH_ID, USER_ID)
VALUES ('2023-09-01', 1, 1);