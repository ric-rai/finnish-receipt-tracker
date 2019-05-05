MERGE INTO receipt VALUES (1, '2019-01-24', 'Biltema', 12.5, 'Ostaja',
                           FILE_READ('classpath:/fi/frt/example-receipts/kuitti1.jpg'));
MERGE INTO purchase VALUES (1, '86953 LIITOSMUHVI 32MM', 1, 4.5, 'Remontti', 1);
MERGE INTO purchase VALUES (2, '86974 LETKUNLIITIN 32MM', 1, 4.5, 'Remontti', 1);
MERGE INTO purchase VALUES (3, '872193 19 X 19 MM. POISTOLETKUILLE 87', 1, 3.5, 'Remontti', 1);

MERGE INTO receipt VALUES (2, '2019-02-03', 'Prisma', 56.42, 'Ostaja',
                           FILE_READ('classpath:/fi/frt/example-receipts/kuitti2.jpg'));
MERGE INTO purchase VALUES (4, 'MUOVIKASSI 40L HARMAA', 1, 0.22, 'Muovikassi', 2);
MERGE INTO purchase VALUES (5, 'WHEY CHOCOLATE', 1, 12.49, 'Lisäravinne', 2);
MERGE INTO purchase VALUES (6, 'KURKUMA', 2, 1.29, 'Ruoka', 2);
MERGE INTO purchase VALUES (7, 'KUIVASHAMPOO', 1, 2.95, 'Kosmetiikka', 2);
MERGE INTO purchase VALUES (8, 'KIRNUVOI LUOMU', 1, 2.39, 'Ruoka', 2);
MERGE INTO purchase VALUES (9, 'ITALIANPATA', 9, 1.59, 'Ruoka', 2);
MERGE INTO purchase VALUES (10, 'VALKOSIPULIVOIPATONKI', 1, 1.25, 'Ruoka', 2);
MERGE INTO purchase VALUES (11, 'SIENIPROTEIINIKUUTIOT', 4, 3.95, 'Ruoka', 2);
MERGE INTO purchase VALUES (12, 'HIUSKIINNE EXTRA STRONG', 1, 2.98, 'Kosmetiikka', 2);
MERGE INTO purchase VALUES (13, 'TUOREJUUSTO VSIP&YRT 12%', 1, 1.45, 'Ruoka', 2);

MERGE INTO receipt VALUES (3, '2019-03-01', 'Puuilo', 14.85, 'Ostaja',
                           FILE_READ('classpath:/fi/frt/example-receipts/kuitti3.jpg'));
MERGE INTO purchase VALUES (14, '10111322 RE-MIX 350G', 1, 2.9, 'Makeinen', 3);
MERGE INTO purchase VALUES (15, '10131725 THE PROTEIN BAR 50G CRISPY BROWNIE BAR', 1, 2.35, 'Ruoka', 3);
MERGE INTO purchase VALUES (16, '10080030 HIOMA-ARKKI MÄRKÄ K80', 1, 0.70, 'Remontti', 3);
MERGE INTO purchase VALUES (17, '10022156 AKRYYLIVÄRISETTI 6 VÄRIÄ', 1, 8.9, 'Taide', 3);

MERGE INTO receipt VALUES (4, '2019-02-17', 'Lidl', 43.22, 'Ostaja',
                           FILE_READ('classpath:/fi/frt/example-receipts/kuitti4.jpg'));
MERGE INTO purchase VALUES (18, 'Andre Jalbert normand. siide', 6, 4.19, 'Siideri', 4);
MERGE INTO purchase VALUES (19, 'Pantti 0,75l', 6, 0.1, 'Pantti', 4);
MERGE INTO purchase VALUES (20, 'Henkselikassi', 1, 0.25, 'Muovikassi', 4);
MERGE INTO purchase VALUES (21, 'Ruukkusalaatti', 1, 0.85, 'Ruoka', 4);
MERGE INTO purchase VALUES (22, 'Suippopaprika punainen irto', 1, 0.26, 'Ruoka', 4);
MERGE INTO purchase VALUES (23, 'Fazer Re-mix', 2, 1.99, 'Makeinen', 4);
MERGE INTO purchase VALUES (24, 'Tomaatti irto', 1, 1.08, 'Ruoka', 4);
MERGE INTO purchase VALUES (25, 'Berliininmunkki', 2, 0.89, 'Ruoka', 4);
MERGE INTO purchase VALUES (26, 'Jumbo vehnäpatonki', 1, 1.19, 'Ruoka', 4);
MERGE INTO purchase VALUES (27, 'Milbona savust. juustoviipal', 1, 1.99, 'Ruoka', 4);
MERGE INTO purchase VALUES (28, 'Voicroissant 27%', 4, 0.39, 'Ruoka', 4);
MERGE INTO purchase VALUES (29, 'LeipäAitta juustosämpylät 5k', 1, 1.89, 'Ruoka', 4);
MERGE INTO purchase VALUES (30, 'TrattoriaAlfredo pizza Mozza', 1, 2.65, 'Ruoka', 4);