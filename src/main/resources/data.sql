MERGE INTO RECEIPT VALUES ( 1, '2019-01-01', 'Kauppa', 10.1, 'Ostaja', null );
MERGE INTO RECEIPT VALUES ( 2, '2019-02-01', 'Kioski', 20.2, 'Ostaja', null );
MERGE INTO RECEIPT VALUES ( 3, '2019-03-01', 'Liike', 30.3, 'Ostaja', null );
MERGE INTO RECEIPT VALUES ( 4, '2019-04-01', 'Prisma', 40.4, 'Ostaja', null );

MERGE INTO PURCHASE VALUES ( 1, 'Salaatti', 1, 1.1, 'Elintarvike', 1 );
MERGE INTO PURCHASE VALUES ( 2, 'Käsipyyhe Serla', 1, 2.2, 'Hygienia', 1 );
MERGE INTO PURCHASE VALUES ( 3, 'Valkosipuli 200g', 1, 3.3, 'Elintarvike', 1 );
MERGE INTO PURCHASE VALUES ( 4, 'Omena irto', 1, 4.4, 'Elintarvike', 1 );

MERGE INTO PURCHASE VALUES ( 5, 'Maito', 1, 1.1, 'Elintarvike', 2 );
MERGE INTO PURCHASE VALUES ( 6, 'Iltalehti', 1, 2.2, 'Lehti', 2 );

MERGE INTO PURCHASE VALUES ( 7, '1011 RE-MIX 350G', 1, 1.1, 'Makeinen', 3 );
MERGE INTO PURCHASE VALUES ( 8, '10022156 AKRYYLIVÄRISETTI 6 VÄRIÄ', 1, 2.2, 'Hygienia', 3 );

MERGE INTO PURCHASE VALUES ( 9, 'Salaatti', 1, 1.1, 'Ruoka', 4 );
MERGE INTO PURCHASE VALUES ( 10, 'Pähkinä', 1, 2.2, 'Ruoka', 4 );
MERGE INTO PURCHASE VALUES ( 11, 'Puhdistusaine', 1, 3.3, 'Siivous', 4 );
MERGE INTO PURCHASE VALUES ( 12, 'Suodatinpussi', 1, 4.4, 'Kahvitarvike', 4 );