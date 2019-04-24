MERGE INTO RECEIPT VALUES ( 1, '2019-01-01', 'Paikka1', 10.1, 'Ostaja1', null );
MERGE INTO RECEIPT VALUES ( 2, '2019-02-01', 'Paikka2', 20.2, 'Ostaja2', null );
MERGE INTO RECEIPT VALUES ( 3, '2019-03-01', 'Paikka3', 30.3, 'Ostaja3', null );
MERGE INTO RECEIPT VALUES ( 4, '2019-04-01', 'Paikka4', 40.4, 'Ostaja4', null );

MERGE INTO PURCHASE VALUES ( 1, 'Ostos1', 1, 1.1, 'Tyyppi1', 1 );
MERGE INTO PURCHASE VALUES ( 2, 'Ostos2', 1, 2.2, 'Tyyppi1', 1 );
MERGE INTO PURCHASE VALUES ( 3, 'Ostos3', 1, 3.3, 'Tyyppi2', 1 );
MERGE INTO PURCHASE VALUES ( 4, 'Ostos4', 1, 4.4, 'Tyyppi2', 1 );