CREATE TABLE CARS (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    PRODUCER VARCHAR(50),
	MODEL VARCHAR(50),
	SPZ VARCHAR(50),
	MANUFACTURED DATE,
	PRICE DECIMAL
 );

CREATE TABLE CUSTOMERS (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    NAME VARCHAR(50),
	SURNAME VARCHAR(50),
	DRIVINGLICENSENUMBER INTEGER,
	IDENTIFICATIONCARDNUMBER INTEGER,
	DEBT DECIMAL
 );

CREATE TABLE ORDERS (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    FROMO DATE,
	TOO DATE
 );
ALTER TABLE ORDERS ADD CONSTRAINT CAR FOREIGN KEY (ID) REFERENCES CARS(ID);
ALTER TABLE ORDERS ADD CONSTRAINT CUSTOMER FOREIGN KEY (ID) REFERENCES CUSTOMERS(ID);

INSERT INTO CARS (PRODUCER,MODEL,SPZ,MANUFACTURED,PRICE) VALUES 
	('Skoda','Fabia','1B0-1234','2010-05-12',1000),
	('VW','Golf','1B0-5678','2010-12-05',1000),
	('Skoda','Octavia','1B0-9012','2010-05-12',1000)
;

SELECT * FROM CARS ORDER BY PRODUCER;

UPDATE CARS SET PRODUCER='Volkswagen' WHERE PRODUCER='VW';

DELETE FROM CARS WHERE MODEL='Octavia';
