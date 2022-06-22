insert into roles (name)
values
('ROLE_ROOT'), ('ROLE_ADMIN'), ('ROLE_USER');

insert into users (username, password, enabled)
values
('root', '$2a$12$r0E4WXE7E1qocNlS1r5.n.YkdByj2le3DElM4MfO8TWcB9Vfy1Sze', 'true');


insert into users_roles (user_id, role_id)
values
(1, 1),
(1, 2),
(1, 3);