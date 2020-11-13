insert into users(first_name, last_name, email, phone_number, password)
values('Maarten', 'De Meyere', 'maarten.demeyere@hotmail.com', '0488270112', 'pass'),
       ('Mira', 'Vogelsang', 'mira.vogelsang@telenet.com', '0470010909', 'pass');

insert into roles(name, rank)
values('Admin', 1), ('User', 2);

insert into userroles(user_id, role_id)
values(1, 1), (2, 2);