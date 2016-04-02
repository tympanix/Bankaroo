--<ScriptOptions statementTerminator=";"/>

ALTER TABLE "DTUGRP09"."Account" DROP CONSTRAINT "Belongs to";

ALTER TABLE "DTUGRP09"."Account" DROP CONSTRAINT "Is Type";

ALTER TABLE "DTUGRP09"."Account" DROP CONSTRAINT "Is exchange-type";

ALTER TABLE "DTUGRP09"."Transaction" DROP CONSTRAINT "For account";

ALTER TABLE "DTUGRP09"."Account" DROP CONSTRAINT "Account_PK";

ALTER TABLE "DTUGRP09"."AccountType" DROP CONSTRAINT "AccountType_PK";

ALTER TABLE "DTUGRP09"."Customer" DROP CONSTRAINT "Customer_PK";

ALTER TABLE "DTUGRP09"."Empleyee" DROP CONSTRAINT "Empleyee_PK";

ALTER TABLE "DTUGRP09"."Exchange" DROP CONSTRAINT "Exchange_PK";

ALTER TABLE "DTUGRP09"."Transaction" DROP CONSTRAINT "Transaction_PK";

DROP TABLE "DTUGRP09"."Account";

DROP TABLE "DTUGRP09"."AccountType";

DROP TABLE "DTUGRP09"."Customer";

DROP TABLE "DTUGRP09"."Empleyee";

DROP TABLE "DTUGRP09"."Exchange";

DROP TABLE "DTUGRP09"."Transaction";

CREATE TABLE "DTUGRP09"."Account" (
		"AccountID" INTEGER NOT NULL, 
		"AccountName" VARCHAR(45), 
		"Balance" DECIMAL(12 , 4) NOT NULL, 
		"CustomerID" INTEGER NOT NULL, 
		"AccountTypeName" VARCHAR(45) NOT NULL, 
		"Currency" CHAR(3) NOT NULL
	)
	AUDIT NONE
	DATA CAPTURE NONE 
	CCSID EBCDIC;

CREATE TABLE "DTUGRP09"."AccountType" (
		"AccountTypeName" VARCHAR(45) NOT NULL, 
		"Interest" DOUBLE
	)
	AUDIT NONE
	DATA CAPTURE NONE 
	CCSID EBCDIC;

CREATE TABLE "DTUGRP09"."Customer" (
		"CustomerID" INTEGER NOT NULL, 
		"CustomerName" VARCHAR(45), 
		"Salt" CHAR(36) NOT NULL, 
		"Password" CHAR(36) NOT NULL
	)
	AUDIT NONE
	DATA CAPTURE NONE 
	CCSID EBCDIC;

CREATE TABLE "DTUGRP09"."Empleyee" (
		"EmployeeID" INTEGER NOT NULL, 
		"EmployeeName" VARCHAR(45), 
		"Salt" CHAR(36) NOT NULL, 
		"Password" CHAR(36) NOT NULL
	)
	AUDIT NONE
	DATA CAPTURE NONE 
	CCSID EBCDIC;

CREATE TABLE "DTUGRP09"."Exchange" (
		"Currency" CHAR(3) NOT NULL, 
		"Rate" DOUBLE NOT NULL
	)
	AUDIT NONE
	DATA CAPTURE NONE 
	CCSID EBCDIC;

CREATE TABLE "DTUGRP09"."Transaction" (
		"TransactionTime" TIMESTAMP NOT NULL, 
		"TransactionName" VARCHAR(45) NOT NULL, 
		"Amount" DECIMAL(12 , 4) NOT NULL, 
		"Balance" DECIMAL(12 , 4) NOT NULL, 
		"AccountID" INTEGER NOT NULL, 
		"CustomerID" INTEGER NOT NULL
	)
	AUDIT NONE
	DATA CAPTURE NONE 
	CCSID EBCDIC;

CREATE UNIQUE INDEX "DTUGRP09"."AccountType_PK"
	ON "DTUGRP09"."AccountType"
	("AccountTypeName");

CREATE UNIQUE INDEX "DTUGRP09"."Account_PK"
	ON "DTUGRP09"."Account"
	("CustomerID", 
	 "AccountID");

CREATE UNIQUE INDEX "DTUGRP09"."Customer_PK"
	ON "DTUGRP09"."Customer"
	("CustomerID");

CREATE UNIQUE INDEX "DTUGRP09"."Empleyee_PK"
	ON "DTUGRP09"."Empleyee"
	("EmployeeID");

CREATE UNIQUE INDEX "DTUGRP09"."Exchange_PK"
	ON "DTUGRP09"."Exchange"
	("Currency");

CREATE UNIQUE INDEX "DTUGRP09"."Transaction_PK"
	ON "DTUGRP09"."Transaction"
	("CustomerID", 
	 "AccountID", 
	 "TransactionTime");

ALTER TABLE "DTUGRP09"."Account" ADD CONSTRAINT "Account_PK" PRIMARY KEY
	("CustomerID", 
	 "AccountID");

ALTER TABLE "DTUGRP09"."AccountType" ADD CONSTRAINT "AccountType_PK" PRIMARY KEY
	("AccountTypeName");

ALTER TABLE "DTUGRP09"."Customer" ADD CONSTRAINT "Customer_PK" PRIMARY KEY
	("CustomerID");

ALTER TABLE "DTUGRP09"."Empleyee" ADD CONSTRAINT "Empleyee_PK" PRIMARY KEY
	("EmployeeID");

ALTER TABLE "DTUGRP09"."Exchange" ADD CONSTRAINT "Exchange_PK" PRIMARY KEY
	("Currency");

ALTER TABLE "DTUGRP09"."Transaction" ADD CONSTRAINT "Transaction_PK" PRIMARY KEY
	("CustomerID", 
	 "AccountID", 
	 "TransactionTime");

ALTER TABLE "DTUGRP09"."Account" ADD CONSTRAINT "Belongs to" FOREIGN KEY
	("CustomerID")
	REFERENCES "DTUGRP09"."Customer"
	("CustomerID")
	ON DELETE CASCADE;

ALTER TABLE "DTUGRP09"."Account" ADD CONSTRAINT "Is Type" FOREIGN KEY
	("AccountTypeName")
	REFERENCES "DTUGRP09"."AccountType"
	("AccountTypeName")
	ON DELETE CASCADE;

ALTER TABLE "DTUGRP09"."Account" ADD CONSTRAINT "Is exchange-type" FOREIGN KEY
	("Currency")
	REFERENCES "DTUGRP09"."Exchange"
	("Currency")
	ON DELETE CASCADE;

ALTER TABLE "DTUGRP09"."Transaction" ADD CONSTRAINT "For account" FOREIGN KEY
	("CustomerID", 
	 "AccountID")
	REFERENCES "DTUGRP09"."Account"
	("CustomerID", 
	 "AccountID")
	ON DELETE CASCADE;

