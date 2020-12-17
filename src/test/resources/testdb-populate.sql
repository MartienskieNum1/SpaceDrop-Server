insert into users(first_name, last_name, email, phone_number, password, planet, country_or_colony, city_or_district, street, number)
values('Mock', 'User', 'user@space.drop', '04586541', '$2a$10$y4ZEbUKH1NBMOneHg.JwrecL8VIyNFfoZcQPsBw2tbriXyIdRzQKW', 'Earth', 'Belgium', 'Bruges', 'SomeStreet', 15),
      ('no-user', 0, 'motloggedin@space.drop', 0, 0, 0, 0, 0, 0, 0),
       ('Maarten', 'De Meyere', 'maarten.demeyere@hotmail.com', '0488270112', 'pass', 'Earth', 'Belgium', 'City', 'Street', 1),
       ('Mira', 'Vogelsang', 'mira.vogelsang@telenet.com', '0470010909', 'pass', 'Earth', 'Belgium', 'City', 'Street', 1);

insert into roles(name, rank)
values('Admin', 1), ('User', 2);

insert into userroles(user_id, role_id)
values(1, 1), (2, 2);

insert into statuses(status)
values ('Travelling'), ('Returning'), ('Finished');

insert into rockets(name, depart_location, departure, arrival, price_per_kilo, max_mass, max_volume, available_mass, available_volume)
values('Falcon Heavy', 'Mars', '2055-12-18 13:30:00', '2055-01-18 08:20:30', 100.0, 10000.0, 2700.0, 10000.0, 2700.0),
        ('Shear Razor', 'Earth', '2055-12-19 12:15:20', '2055-01-19 22:30:00', 120.0, 15000.0, 1100.0, 15000.0, 1100.0),
      ('Starship', 'Mars', '2055-12-20 12:15:20', '2055-01-20 22:30:00', 110.0, 1600.0, 1150.0, 1600.0, 1150.0),
      ('Cataphract', 'Earth', '2055-12-21 12:15:20', '2055-01-21 22:30:00', 115.0, 16000.0, 5000.0, 16000.0, 5000.0),
      ('Maiden Voyage', 'Mars', '2055-12-22 12:15:20', '2055-01-22 22:30:00', 110.0, 1300.0, 560.0, 1300.0, 560.0),
      ('Aquitaine', 'Earth', '2055-12-23 12:15:20', '2055-01-23 22:30:00', 120.0, 13250.0, 6345.0, 13250.0, 6345.0),
      ('The Ark', 'Mars', '2055-12-24 12:15:20', '2055-01-24 22:30:00', 130.0, 1578.0, 895.0, 1578.0, 895.0),
      ('Tantive IV', 'Earth', '2055-12-25 12:15:20', '2055-01-25 22:30:00', 100.0, 6947.0, 5695.0, 6947.0, 5695.0),
      ('Millenial Hawk', 'Mars', '2055-12-26 12:15:20', '2055-01-26 22:30:00', 120.0, 456.0, 410.0, 456.0, 410.0),
      ('Paramount', 'Mars', '2055-12-27 12:15:20', '2055-01-27 22:30:00', 115.0, 1689.0, 1536.0, 1689.0, 1536.0),
      ('Dreadnaught', 'Earth', '2055-12-28 12:15:20', '2055-01-28 22:30:00', 125.0, 8915.0, 6836.0, 8915.0, 6836.0),
      ('Destiny', 'Mars', '2055-12-29 12:15:20', '2055-01-29 22:30:00', 105.0, 5492.0, 6825.0, 5492.0, 6825.0),
      ('Vagabond', 'Earth', '2055-12-30 12:15:20', '2055-01-30 22:30:00', 110.0, 765.0, 834.0, 765.0, 834.0),
      ('ISS Praetor', 'Mars', '2055-12-31 12:15:20', '2055-01-31 22:30:00', 115.0, 7852.0, 5942.0, 7852.0, 5942.0),
      ('SC Verminus', 'Earth', '2055-01-01 12:15:20', '2055-02-01 22:30:00', 130.0, 4832.0, 4892.0, 4832.0, 4892.0),
      ('Aurora', 'Mars', '2055-01-02 12:15:20', '2055-02-02 22:30:00', 105.0, 3654.0, 2541.0, 3654.0, 2541.0),
      ('Galactica', 'Earth', '2055-01-03 12:15:20', '2055-02-03 22:30:00', 113.0, 8652.0, 5234.0, 8652.0, 5234.0),
      ('Pegasus', 'Mars', '2055-01-04 12:15:20', '2055-02-04 22:30:00', 126.0, 8652.0, 5234.0, 8652.0, 5234.0),
      ('SC Galatea', 'Earth', '2055-01-05 12:15:20', '2055-02-05 22:30:00', 116.0, 4692.0, 3915.0, 4692.0, 3915.0),
      ('ISS Raven', 'Mars', '2055-01-06 12:15:20', '2055-02-06 22:30:00', 104.0, 158.0, 125.0, 158.0, 125.0),
      ('CS Muriela', 'Earth', '2055-01-07 12:15:20', '2055-02-07 22:30:00', 131.0, 15060.0, 14986.0, 15060.0, 14986.0),
      ('SC Dark Phoenix', 'Mars', '2055-01-08 12:15:20', '2055-02-08 22:30:00', 126.0, 3682.0, 3124.0, 3682.0, 3124.0),
      ('Genesis', 'Earth', '2055-01-09 12:15:20', '2055-02-09 22:30:00', 113.0, 6826.0, 6254.0, 6826.0, 6254.0),
      ('Remorseless', 'Mars', '2055-01-10 12:15:20', '2055-02-10 22:30:00', 103.0, 2657.0, 3578.0, 2657.0, 3578.0),
      ('SS Valhala', 'Earth', '2055-01-11 12:15:20', '2055-02-11 22:30:00', 127.0, 1587.0, 876.0, 1587.0, 876.0),
      ('SC Duke', 'Mars', '2055-01-12 12:15:20', '2055-02-12 22:30:00', 100.0, 7512.0, 6952.0, 7512.0, 6952.0),
      ('Fate', 'Earth', '2055-01-13 12:15:20', '2055-02-13 22:30:00', 110.0, 9532.0, 7621.0, 9532.0, 7621.0),
      ('Exodar', 'Mars', '2055-01-14 12:15:20', '2055-02-14 22:30:00', 114.0, 8625.0, 8624.0, 8625.0, 8624.0),
      ('Saratoga', 'Earth', '2055-01-15 12:15:20', '2055-02-15 22:30:00', 102.0, 4689.0, 3654.0, 4689.0, 3654.0),
      ('Untouchable', 'Mars', '2055-01-16 12:15:20', '2055-02-16 22:30:00', 112.0, 4589.0, 2678.0, 4589.0, 2678.0),
      ('Xerxes', 'Earth', '2055-01-16 12:15:20', '2055-02-16 22:30:00', 120.0, 687.0, 524.0, 687.0, 524.0),
      ('Thylacine', 'Mars', '2055-01-17 12:15:20', '2055-02-17 22:30:00', 119.0, 5682.0, 4925.0, 5682.0, 4925.0),
      ('Titan', 'Earth', '2055-01-18 12:15:20', '2055-02-18 22:30:00', 106.0, 3659.0, 4265.0, 3659.0, 4265.0),
      ('Falcon Heavy', 'Mars', '2055-01-19 13:30:00', '2055-02-19 08:20:30', 100.0, 10000.0, 2700.0, 10000.0, 2700.0),
      ('Shear Razor', 'Earth', '2055-01-20 12:15:20', '2055-02-20 22:30:00', 120.0, 15000.0, 1100.0, 15000.0, 1100.0),
      ('Starship', 'Mars', '2055-01-21 12:15:20', '2055-02-21 22:30:00', 110.0, 1600.0, 1150.0, 1600.0, 1150.0),
      ('Cataphract', 'Earth', '2055-01-22 12:15:20', '2055-02-22 22:30:00', 115.0, 16000.0, 5000.0, 16000.0, 5000.0),
      ('Maiden Voyage', 'Mars', '2055-01-23 12:15:20', '2055-02-23 22:30:00', 110.0, 1300.0, 560.0, 1300.0, 560.0),
      ('Aquitaine', 'Earth', '2055-01-24 12:15:20', '2055-02-24 22:30:00', 120.0, 13250.0, 6345.0, 13250.0, 6345.0),
      ('The Ark', 'Mars', '2055-01-25 12:15:20', '2055-01-25 22:30:00', 130.0, 1578.0, 895.0, 1578.0, 895.0),
      ('Tantive IV', 'Earth', '2055-01-26 12:15:20', '2055-02-26 22:30:00', 100.0, 6947.0, 5695.0, 6947.0, 5695.0),
      ('Millenial Hawk', 'Mars', '2055-01-27 12:15:20', '2055-02-27 22:30:00', 120.0, 456.0, 410.0, 456.0, 410.0),
      ('Paramount', 'Mars', '2055-01-28 12:15:20', '2055-02-28 22:30:00', 115.0, 1689.0, 1536.0, 1689.0, 1536.0),
      ('Dreadnaught', 'Earth', '2055-01-29 12:15:20', '2055-03-01 22:30:00', 125.0, 8915.0, 6836.0, 8915.0, 6836.0),
      ('Destiny', 'Mars', '2055-01-30 12:15:20', '2055-03-02 22:30:00', 105.0, 5492.0, 6825.0, 5492.0, 6825.0),
      ('Vagabond', 'Earth', '2055-01-31 12:15:20', '2055-03-03 22:30:00', 110.0, 765.0, 834.0, 765.0, 834.0),
      ('ISS Praetor', 'Mars', '2055-02-01 12:15:20', '2055-03-04 22:30:00', 115.0, 7852.0, 5942.0, 7852.0, 5942.0),
      ('SC Verminus', 'Earth', '2055-02-02 12:15:20', '2055-03-05 22:30:00', 130.0, 4832.0, 4892.0, 4832.0, 4892.0),
      ('Aurora', 'Mars', '2055-02-03 12:15:20', '2055-03-06 22:30:00', 105.0, 3654.0, 2541.0, 3654.0, 2541.0),
      ('Galactica', 'Earth', '2055-02-04 12:15:20', '2055-03-07 22:30:00', 113.0, 8652.0, 5234.0, 8652.0, 5234.0),
      ('Pegasus', 'Mars', '2055-02-05 12:15:20', '2055-03-08 22:30:00', 126.0, 8652.0, 5234.0, 8652.0, 5234.0),
      ('SC Galatea', 'Earth', '2055-02-06 12:15:20', '2055-03-09 22:30:00', 116.0, 4692.0, 3915.0, 4692.0, 3915.0),
      ('ISS Raven', 'Mars', '2055-02-07 12:15:20', '2055-03-10 22:30:00', 104.0, 158.0, 125.0, 158.0, 125.0),
      ('CS Muriela', 'Earth', '2055-02-08 12:15:20', '2055-03-11 22:30:00', 131.0, 15060.0, 14986.0, 15060.0, 14986.0),
      ('SC Dark Phoenix', 'Mars', '2055-02-09 12:15:20', '2055-03-12 22:30:00', 126.0, 3682.0, 3124.0, 3682.0, 3124.0),
      ('Genesis', 'Earth', '2055-02-10 12:15:20', '2055-03-13 22:30:00', 113.0, 6826.0, 6254.0, 6826.0, 6254.0),
      ('Remorseless', 'Mars', '2055-02-11 12:15:20', '2055-03-14 22:30:00', 103.0, 2657.0, 3578.0, 2657.0, 3578.0),
      ('SS Valhala', 'Earth', '2055-02-12 12:15:20', '2055-03-15 22:30:00', 127.0, 1587.0, 876.0, 1587.0, 876.0),
      ('SC Duke', 'Mars', '2055-02-13 12:15:20', '2055-03-16 22:30:00', 100.0, 7512.0, 6952.0, 7512.0, 6952.0),
      ('Fate', 'Earth', '2055-02-14 12:15:20', '2055-03-17 22:30:00', 110.0, 9532.0, 7621.0, 9532.0, 7621.0),
      ('Exodar', 'Mars', '2055-02-15 12:15:20', '2055-03-18 22:30:00', 114.0, 8625.0, 8624.0, 8625.0, 8624.0),
      ('Saratoga', 'Earth', '2055-02-16 12:15:20', '2055-03-19 22:30:00', 102.0, 4689.0, 3654.0, 4689.0, 3654.0),
      ('Untouchable', 'Mars', '2055-02-17 12:15:20', '2055-03-20 22:30:00', 112.0, 4589.0, 2678.0, 4589.0, 2678.0),
      ('Xerxes', 'Earth', '2055-02-18 12:15:20', '2055-03-21 22:30:00', 120.0, 687.0, 524.0, 687.0, 524.0),
      ('Thylacine', 'Earth', '2055-02-19 12:15:20', '2055-03-23 22:30:00', 119.0, 5682.0, 4925.0, 5682.0, 4925.0),
      ('Titan', 'Earth', '2055-02-20 12:15:20', '2055-03-24 22:30:00', 106.0, 3659.0, 4265.0, 3659.0, 4265.0),


insert into orders(user_id, rocket_id, status_id, mass, width, height, depth, cost, planet, country_or_colony, city_or_district, street, number)
values (1, 1, 2, 50, 20, 20, 15, 100, 'Earth', 'Belgium', 'City', 'Street', 1),
       (1, 2, 1, 150, 75, 82, 31, 150, 'Earth', 'Belgium', 'City', 'Street', 1);