CREATE TABLE CAR (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    PRODUCER VARCHAR(50),
	MODEL VARCHAR(50),
	SPZ VARCHAR(50),
	MANUFACTURED DATE,
	PRICE DECIMAL
 );

CREATE TABLE CUSTOMER (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    NAME VARCHAR(50),
	SURNAME VARCHAR(50),
	DRIVINGLICENSENUMBER INTEGER,
	IDENTIFICATIONCARDNUMBER INTEGER,
	DOUBT DECIMAL
 );

CREATE TABLE ORDER (
    ID BIGINT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    FROM DATE,
	TO DATE,
	CAR BIGINT FOREIGN KEY,
	CUSTOMER BIGINT FOREIGN KEY
 );

INSERT INTO CAR (PRODUCER,MODEL,SPZ,MANUFACTURED,PRICE) VALUES 
	('Skoda','Fabia','1B0-1234','2010-05-12',1000),
	('VW','Golf','1B0-5678','2010-12-05',1000),
	('Skoda','Octavia','1B0-9012','2010-05-12',1000)
;

SELECT PRODUCER+MODEL AS CAR_NAME,PRICE FROM CAR ORDER BY PRICE;

UPDATE CAR SET PRODUCER='Volkswagen' WHERE PRODUCER='VW';

DELETE FROM CAR WHERE MODEL='Octavia';