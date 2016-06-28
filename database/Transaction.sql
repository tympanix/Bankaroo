CREATE PROCEDURE Transaction (IN vAmountForeign DECIMAL(16,4), IN vCurrency CHAR(3), 
								IN vAccountIDFrom INT, IN vAccountIDTo INT,
								IN vTransactionMessageFrom VARCHAR(45), IN vTransactionMessageTo VARCHAR(45))
	VERSION V1
	ISOLATION LEVEL CS
	RESULT SETS 1
	LANGUAGE SQL
P1: BEGIN
  /* Variables*/
  DECLARE CurrTime TIMESTAMP(2);
  DECLARE GeneratedTransID INT;
  DECLARE BalanceBeforeFrom DECIMAL(16,4);
  DECLARE BalanceBeforeTo DECIMAL(16,4);
  DECLARE BalanceAfterFrom DECIMAL(16,4);
  DECLARE BalanceAfterTo DECIMAL(16,4);
  DECLARE AmountLocal DECIMAL(16,4);
  DECLARE vRate DECIMAL(6,2);
  
   /* Variables are assigned values for insertion in History later*/
  SELECT "Balance" INTO BalanceBeforeFrom FROM  "Account" WHERE "AccountID" = vAccountIDFrom;
  SELECT "Balance" INTO BalanceBeforeTo FROM  "Account" WHERE "AccountID" = vAccountIDTo;
  SELECT "Rate" INTO vRate FROM "Exchange" WHERE  "Currency" = vCurrency;
  SET AmountLocal = vAmountForeign/100*vRate;
  
  /* Calculates the balance after the transaction. This calculations require the rate to be given in 100 DKK's*/
  SET BalanceAfterFrom = (BalanceBeforeFrom - AmountLocal);
  SET BalanceAfterTo = (BalanceBeforeTo + AmountLocal);
  
   /* Updates the accounts*/
   UPDATE "Account" SET "Balance" = BalanceAfterFrom WHERE "AccountID" = vAccountIDFrom;
   UPDATE "Account" SET "Balance" = BalanceAfterTo WHERE "AccountID" = vAccountIDTo;
  
  /* Variables are assigned for the transaction and the transaction is logged*/
  SELECT TIMESTAMP(CURRENT_TIMESTAMP, 2) INTO CurrTime FROM SYSIBM.SYSDUMMY1;
  SELECT "TransactionID" INTO GeneratedTransID FROM FINAL TABLE(
  			INSERT INTO "Transaction" VALUES(Default, CurrTime, vAccountIDFrom, vAccountIDTo, vAmountForeign, vCurrency)
  			);
  			 
 /* The transaction is logged in the History for each account */
  INSERT INTO "History" VALUES (GeneratedTransID, BalanceAfterFrom, vAccountIDFrom, AmountLocal, 'W', vTransactionMessageFrom);
  INSERT INTO "History" VALUES (GeneratedTransID, BalanceAfterTo, vAccountIDTo, AmountLocal, 'D', vTransactionMessageTo);
	COMMIT;
	
END P1