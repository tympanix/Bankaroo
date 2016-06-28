SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

DELETE FROM "DTUGRP09"."User";
DELETE FROM "DTUGRP09"."City";
DELETE FROM "DTUGRP09"."UserRoles";
DELETE FROM "DTUGRP09"."Permissions";
DELETE FROM "DTUGRP09"."AccountType";
DELETE FROM "DTUGRP09"."Account";
DELETE FROM "DTUGRP09"."Exchange";
DELETE FROM "DTUGRP09"."Transaction";
DELETE FROM "DTUGRP09"."History";

								/*Postal Codes*/
INSERT INTO "DTUGRP09"."City" VALUES(3480, 'Fredensborg');
INSERT INTO "DTUGRP09"."City" VALUES(3400, 'Hillerød');
INSERT INTO "DTUGRP09"."City" VALUES(2800, 'Kgs. Lyngby');
INSERT INTO "DTUGRP09"."City" VALUES(4000, 'Roskilde');
INSERT INTO "DTUGRP09"."City" VALUES(2400, 'København NV');
INSERT INTO "DTUGRP09"."City" VALUES(6500, 'Vojens');

								/*Users*/		

INSERT INTO "DTUGRP09"."User" VALUES(1111111111, 'Marc Larsen', 6500,'Lejlighedsvej',12345678,
'marcmus@bispebjerg.dk', 
'9750a2221c51560ad44c765c1356204308b0a4ffd1b9c7d461ecb522bdc7d518', 
'99480dbda3c54edd46f51c51b0b91060460fb95f73c791cddc9efb3575d1e5b6');
INSERT INTO "DTUGRP09"."User" VALUES(2222222222, 'Gabriel Broholm', 2400,'Nørrebro 23',87654321,
'gugundi@kebab.dk', 
'ceb74d1707331918072928d68658646c0bf64e799510660e593532944dd6c922', 
'a5ed9e6d1fc53e9bd8cc7837320df6d0b930fe6e2971b403bb857a79a31e50ff');
INSERT INTO "DTUGRP09"."User" VALUES(3333333333, 'Marcus Hansen', 2800,'Nærumvej',88888888,
'marco@polo.com', 
'ceb74d1707331918072928d68658646c0bf64e799510660e593532944dd6c922', 
'a5ed9e6d1fc53e9bd8cc7837320df6d0b930fe6e2971b403bb857a79a31e50ff');					
INSERT INTO "DTUGRP09"."User" VALUES(1234567890, 'Gustavo Madslund', 2800,'Kampsax',23242526,
'gustavo@spainmail.eu', 
'ceb74d1707331918072928d68658646c0bf64e799510660e593532944dd6c922', 
'a5ed9e6d1fc53e9bd8cc7837320df6d0b930fe6e2971b403bb857a79a31e50ff');
INSERT INTO "DTUGRP09"."User" VALUES(9876543210, 'Mathiiiias Mortensen', 4000,'Svinvænget',13371337,
'svinet@bacon.dk', 
'ceb74d1707331918072928d68658646c0bf64e799510660e593532944dd6c922', 
'a5ed9e6d1fc53e9bd8cc7837320df6d0b930fe6e2971b403bb857a79a31e50ff');
INSERT INTO "DTUGRP09"."User" VALUES(9999999999, 'Simonze Pileborg', 3480,'Jernbanegade',48484848,
'togtossen@nordsjaelland.dk', 
'ceb74d1707331918072928d68658646c0bf64e799510660e593532944dd6c922', 
'a5ed9e6d1fc53e9bd8cc7837320df6d0b930fe6e2971b403bb857a79a31e50ff');

INSERT INTO "DTUGRP09"."User" VALUES(99999999999, 'Simonze Pileborg', 3480,'Jernbanegade',48484848,
'togtossen@nordsjaelland.dk', 
'ceb74d1707331918072928d68658646c0bf64e799510660e593532944dd6c922', 
'a5ed9e6d1fc53e9bd8cc7837320df6d0b930fe6e2971b403bb857a79a31e50ff');

				/*Permissions*/
INSERT INTO "DTUGRP09"."Permissions" VALUES('Employee', 'The employee has acces to all wanted administration-ressources');

				/*User roles*/			
INSERT INTO "DTUGRP09"."UserRoles" VALUES(1111111111,'Employee');
INSERT INTO "DTUGRP09"."UserRoles" VALUES(2222222222,'Employee');
INSERT INTO "DTUGRP09"."UserRoles" VALUES(3333333333,'Employee');

									/* AccountTypeName, Interest*/
INSERT INTO "DTUGRP09"."AccountType" VALUES('Savings', 1.00);
INSERT INTO "DTUGRP09"."AccountType" VALUES('Deposit', 0.25);
									/* Currency, Rate*/
INSERT INTO "DTUGRP09"."Exchange" VALUES('DKK', 100);
INSERT INTO "DTUGRP09"."Exchange" VALUES('EUR', 750);
INSERT INTO "DTUGRP09"."Exchange" VALUES('GBP', 950);
INSERT INTO "DTUGRP09"."Exchange" VALUES('USD', 650);
									/* AccountID, AccountName, Balance, CustomerID, AccountType, Currency */							
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'Opsaring', 50000, 1234567890, 'Savings', 'DKK');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'The guldgrotte', 10000, 1234567890, 'Deposit', 'USD');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'Sir Deposit', 1337, 9876543210, 'Deposit', 'EUR');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'Opsaring til kunstig hofte', 1, 9999999999, 'Savings', 'DKK');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'Min lønkonto', 13371337, 1234567890, 'Deposit', 'DKK');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'My glorious opsparing', 2000000, 9999999999, 'Savings', 'GBP');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'Min dejlige opsparing', 2345812, 9876543210, 'Savings', 'DKK');
INSERT INTO "DTUGRP09"."Account" VALUES(Default, 'Løn!!!', 20.74, 9999999999, 'Deposit', 'DKK');

/*DELETE FROM "DTUGRP09"."Account"  WHERE "AccountID"=13;*/ 

/* Call procedure for transactions*/
CALL TRANSACTION(10 ,'DKK',1,2,'Hej','Hej');

CALL TRANSACTION(200,'DKK',2,3,'Hej','Hej');
CALL TRANSACTION(200,'DKK',5,8,'Hej','Hej');
CALL TRANSACTION(1337,'DKK',3,6,'Hej','Hej');
CALL TRANSACTION(1,'DKK',1,4,'Hej','Hej');
CALL TRANSACTION(1337,'USD',2,3,'Bytte?','Bytte?');
CALL TRANSACTION(1337,'EUR',3,2,'BYTTE!','BYTTE!');
CALL TRANSACTION(50000,'DKK',5,8,'Mor <3','Til min dejlige mor');
CALL TRANSACTION(33,'DKK',1,2,'H','H');
CALL TRANSACTION(34,'DKK',1,3,'e','e');
CALL TRANSACTION(36,'DKK',1,4,'j','j');
CALL TRANSACTION(37,'DKK',2,5,'?','!');


									/* TransactionID, TransactionTime, AccountIDFrom, AccountIDTo, 
									TransactionMessageFrom, TransactionMessageTo, AmountForeign, Currency */
/*
INSERT INTO "DTUGRP09"."Transaction" VALUES(Default, '2016-06-23 12:00:00', 2, 3, 1000, 'DKK');
INSERT INTO "DTUGRP09"."Transaction" VALUES(Default, '2016-06-23 12:10:00', 2, 24, 150, 'DKK');
INSERT INTO "DTUGRP09"."Transaction" VALUES(Default, '2016-06-23 13:37:00', 3, 24, 250, 'EUR');
*/
									/* TransactionID, Balance, AccountID, AmountLocal, TransactionType*/
									/*
INSERT INTO "DTUGRP09"."History" VALUES (1, 10000, 2, 1000, 'W', 'Penge til Mathiias');
INSERT INTO "DTUGRP09"."History" VALUES (1, 1337, 3, 1000, 'D', 'Penge fra Gustavo');
INSERT INTO "DTUGRP09"."History" VALUES (2, 9000, 2, 150, 'W', 'Penge til Simze');
INSERT INTO "DTUGRP09"."History" VALUES (2, 20000, 24, 150, 'D', 'Penge fra Gustavo');
INSERT INTO "DTUGRP09"."History" VALUES (3, 2337, 3, 250, 'W', 'Penge til Simze');
INSERT INTO "DTUGRP09"."History" VALUES (3, 20150, 24, 250, 'D', 'Penge fra Mathiias');

*/
/*OBSOLETE*/
/* EmployeeID, EmployeeName, Salt, Password*/
/*INSERT INTO "DTUGRP09"."Employee" VALUES(1, 'Marc Larsen', '123456', 'marcerdenbedste');
INSERT INTO "DTUGRP09"."Employee" VALUES(2, 'Gabriel Broholm', '654321', 'gugundierdenbedste');
INSERT INTO "DTUGRP09"."Employee" VALUES(3, 'Marcus Hansen', '13371337', 'poloerdenbedste');*/
									/* CustomerID, CustomerName, Salt, Password, EmployeeID*/
