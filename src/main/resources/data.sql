INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user', 'user@yandex.ru', 'user'),
       ('admin', 'admin@gmail.com', 'admin');

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 'USER'),
       (2, 'USER'),
       (2, 'ADMIN');

INSERT INTO RESTAURANT (NAME)
VALUES ('Burger King'),
       ('KFC'),
       ('Subway'),
       ('Dodo Pizza');

INSERT INTO DISH (PRICE, RESTAURANT_ID, NAME)
VALUES (150, 1, 'burger'),
       (200, 1, 'cheeseburger'),
       (300, 2, 'bucket'),
       (100, 2, 'fries'),
       (200, 3, 'subway club'),
       (220, 3, 'roast beef'),
       (300, 4, 'dodo'),
       (280, 4, 'veggie');

INSERT INTO LUNCH (DATE, RESTAURANT_ID)
VALUES ('2020-09-01', 1),
       ('2020-09-01', 2),
       ('2020-09-01', 3),
       ('2020-09-01', 4);

INSERT INTO LUNCH_ITEM (DISH_ID, LUNCH_ID)
VALUES (1, 1),
       (3, 2),
       (5, 3),
       (7, 4);

INSERT INTO VOTE (DATE, LUNCH_ID, USER_ID)
VALUES ('2020-09-01', 1, 1);