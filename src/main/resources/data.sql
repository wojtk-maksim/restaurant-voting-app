INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('user', 'user@yandex.ru', 'user'),
       ('admin', 'admin@gmail.com', 'admin');

INSERT INTO USER_ROLE (USER_ID, ROLE)
VALUES (1, 'USER'),
       (2, 'USER'),
       (2, 'ADMIN');