insert into users(first_name, last_name, email, phone_number, password, planet, country_or_colony, city_or_district, street, number)
values('Maarten', 'De Meyere', 'maarten.demeyere@hotmail.com', '0488270112', 'pass', 'Earth', 'Belgium', 'City', 'Street', 1),
       ('Mira', 'Vogelsang', 'mira.vogelsang@telenet.com', '0470010909', 'pass', 'Earth', 'Belgium', 'City', 'Street', 1);

insert into roles(name, rank)
values('Admin', 1), ('User', 2);

insert into userroles(user_id, role_id)
values(1, 1), (2, 2);

insert into statuses(status)
values ('Travelling'), ('Returning'), ('Finished');

insert into rockets(name, departure, arrival, price_per_kilo, max_mass, max_volume, available_mass, available_volume)
values('Falcon Heavy', '2057-06-05 13:30:00', '2057-07-05 08:20:30', 100.0, 100000.0, 270.0, 100000.0, 270.0),
        ('Starship', '2056-02-13 12:15:20', '2056-03-15 22:30:00', 120.0, 150000.0, 1100.0, 150000.0, 1100.0);

insert into orders(user_id, rocket_id, status_id, mass, width, height, depth, cost)
values (1, 1, 2, 50, 20, 20, 15, 100), (1, 1, 1, 150, 75, 82, 31, 150);