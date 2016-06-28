--<ScriptOptions statementTerminator=";"/>

/*NOTE: View skal laves her, da Wizard i Data Studio fucker op...*/
SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

DROP VIEW "DTUGRP09"."HistoryView";

CREATE VIEW "HistoryView" AS (SELECT "AccountID", "Balance", "AmountLocal", 
"TransactionType", "Message", "TransactionTime" FROM "History" INNER JOIN "Transaction" 
ON "History"."TransactionID" = "Transaction"."TransactionID");

/*Show the view HistoryView*/
--<ScriptOptions statementTerminator=";"/>
SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

SELECT * FROM HistoryView;

/*NOTE: View skal laves her, da Wizard i Data Studio fucker op...*/
SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

DROP VIEW "DTUGRP09"."UserView";

CREATE VIEW "UserView" AS (SELECT "UserID", "UserName", "User"."PostalCode", "City", 
"Address", "Phone", "Email", "Salt", "Password" FROM "User", "City" WHERE 
"User"."PostalCode" = "City"."PostalCode");

SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

SELECT * FROM UserView;

/*Trigger for handling negative amounts in transaction*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."NoNegativeAmount"#

CREATE TRIGGER "DTUGRP09"."NoNegativeAmount" 
	NO CASCADE BEFORE INSERT ON "DTUGRP09"."Transaction"
	REFERENCING  NEW AS NEW
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."AmountForeign" < 0)
SIGNAL SQLSTATE '75001' ('ERROR: Negative amounts are not allowed')#

/*Trigger for handling negative balance in account*/
--<ScriptOptions statementTerminator="#"/>

SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."NoNegativeBalance"#

CREATE TRIGGER "DTUGRP09"."NoNegativeBalance" 
	NO CASCADE BEFORE INSERT ON "DTUGRP09"."Account"
	REFERENCING  NEW AS NEW
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."Balance"<0)
SIGNAL SQLSTATE '75001' ('ERROR: A negative balance is not allowed')#

/*Trigger for handling negative balance updates in account*/
--<ScriptOptions statementTerminator="#"/>

SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."NoUpdateNegativeBalance"#

CREATE TRIGGER "DTUGRP09"."NoUpdateNegativeBalance" 
	NO CASCADE BEFORE UPDATE OF "Balance" ON "DTUGRP09"."Account"
	REFERENCING  NEW AS NEW
		OLD AS OLD
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."Balance"<0)
SIGNAL SQLSTATE '75001' ('ERROR: A negative balance is not allowed')#


/*Trigger for ensuring correct lenght of CPR-number*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."ValidCPR"#

CREATE TRIGGER "DTUGRP09"."ValidCPR" 
	NO CASCADE BEFORE INSERT ON "DTUGRP09"."User"
	REFERENCING  NEW AS NEW
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."UserID" NOT BETWEEN 1000000000 AND 9999999999

)
SIGNAL SQLSTATE '75001' ('ERROR: Your CPR-number must be 10 digits long (without \"- \" )')#

/*Trigger for ensuring correct lenght of Postal Code*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."PositivePostal"#

CREATE TRIGGER "DTUGRP09"."PositivePostal" 
	NO CASCADE BEFORE INSERT ON "DTUGRP09"."City"
	REFERENCING  NEW AS NEW
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."PostalCode" NOT BETWEEN 0000 AND 9999)
SIGNAL SQLSTATE '75001' ('ERROR: Please enter a valid 4-digit danish postal code')#

/*Trigger for ensuring correct update lenght of Postal Code*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."PositivePostalUpdate"#

CREATE TRIGGER "DTUGRP09"."PositivePostalUpdate" 
	NO CASCADE BEFORE UPDATE OF "PostalCode" ON "DTUGRP09"."City"
	REFERENCING  NEW AS NEW
			OLD AS OLD
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."PostalCode" NOT BETWEEN 0000 AND 9999)
SIGNAL SQLSTATE '75001' ('ERROR: Please enter a valid 4-digit danish postal code')#

/*Trigger for ensuring correct lenght of phone number*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."ValidPhone"#

CREATE TRIGGER "DTUGRP09"."ValidPhone" 
	NO CASCADE BEFORE INSERT ON "DTUGRP09"."User"
	REFERENCING  NEW AS NEW
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."Phone" NOT BETWEEN 10000000 AND 99999999)
SIGNAL SQLSTATE '75001' ('ERROR: Please enter a valid 8-digit danish phone number (without country code)')#

/*Trigger for ensuring correct update lenght of phone number*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."ValidPhoneUpdate"#

CREATE TRIGGER "DTUGRP09"."ValidPhoneUpdate" 
	NO CASCADE BEFORE UPDATE OF "Phone" ON "DTUGRP09"."User"
	REFERENCING  NEW AS NEW
		OLD AS OLD
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (NEW."Phone" NOT BETWEEN 10000000 AND 99999999)
SIGNAL SQLSTATE '75001' ('ERROR: Please enter a valid 8-digit danish phone number (without country code)')#


/*Trigger for ensuring that accounts only can be deleted, if their balance equals to 0*/
--<ScriptOptions statementTerminator="#"/>
SET SCHEMA DTUGRP09#

SET CURRENT SQLID = 'DTUGRP09'#

DROP TRIGGER "DTUGRP09"."DeleteAccount"#

CREATE TRIGGER "DTUGRP09"."DeleteAccount" 
	NO CASCADE BEFORE DELETE ON "DTUGRP09"."Account"
	REFERENCING  OLD AS OLD
	FOR EACH ROW MODE DB2SQL
	NOT SECURED
WHEN (OLD."Balance" != 0)
SIGNAL SQLSTATE '75001' ('ERROR: An account with dept cannot be deleted')#

/* Creates backup-tables */
--<ScriptOptions statementTerminator=";"/>
SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

DROP TABLE "DTUGRP09"."BackupTransaction";

DROP TABLE "DTUGRP09"."BackupHistory";

CREATE TABLE "DTUGRP09"."BackupTransaction" LIKE "DTUGRP09"."Transaction";

CREATE TABLE "DTUGRP09"."BackupHistory" LIKE "DTUGRP09"."History";

/* Se event*/
--<ScriptOptions statementTerminator=";"/>
SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';
SELECT * FROM TABLE(DSNADM.ADMIN_TASK_LIST()) AS X;

/* Event status*/
select * from table( dsnadm.admin_task_status() ) as x;

/* Call to TransactionProcedure*/
--<ScriptOptions statementTerminator=";"/>
/*
SET SCHEMA DTUGRP09;

SET CURRENT SQLID = 'DTUGRP09';

CALL TRANSACTION(200.00,'DKK',2,3,'Hej','Hej');
*/


