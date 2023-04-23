
CREATE TABLE people(id SERIAL PRIMARY KEY, email text NOT NULL, UNIQUE(email));

CREATE TABLE submitted_toppings(person_id int NOT NULL, topping_id int NOT NULL, PRIMARY KEY(person_id, topping_id));

CREATE TABLE toppings(id SERIAL PRIMARY KEY, name text NOT NULL, UNIQUE(name));

