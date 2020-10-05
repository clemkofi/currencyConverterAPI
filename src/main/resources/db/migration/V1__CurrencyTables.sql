CREATE TABLE currency
(
    id serial NOT NULL,
    name character varying(20) NOT NULL,
    symbol character varying(3) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE currency_values
(
    primary_currency integer NOT NULL,
    secondary_currency integer NOT NULL,
    value real
);

INSERT INTO 
	currency(name, symbol) 
VALUES
	('EUR', '€'),
	('USD', '$'),
	('GHS', '₵'),
	('GBP', '£'),
	('JBY', '¥'),
	('INR', '₹'),
	('ZAR', 'R');

INSERT INTO 
	currency_values(primary_currency, secondary_currency, value)
VALUES
	(1, 2, null), 
	(1, 3, null), 
	(1, 4, null), 
	(1, 5, null), 
	(1, 6, null), 
	(1, 7, null), 
	(2, 1, null), 
	(2, 3, null), 
	(2, 4, null), 
	(2, 5, null), 
	(2, 6, null), 
	(2, 7, null), 
	(3, 1, null),
	(3, 2, null), 
	(3, 4, null), 
	(3, 5, null), 
	(3, 6, null), 
	(3, 7, null), 
	(4, 1, null), 
	(4, 2, null), 
	(4, 3, null), 
	(4, 5, null), 
	(4, 6, null), 
	(4, 7, null), 
	(5, 1, null), 
	(5, 2, null), 
	(5, 3, null), 
	(5, 4, null), 
	(5, 6, null), 
	(5, 7, null), 
	(6, 1, null), 
	(6, 2, null), 
	(6, 3, null), 
	(6, 4, null), 
	(6, 5, null), 
	(6, 7, null), 
	(7, 1, null), 
	(7, 2, null), 
	(7, 3, null), 
	(7, 4, null), 
	(7, 5, null), 
	(7, 6, null);