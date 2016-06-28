CREATE PROCEDURE DeleteAccount (IN vAccountIDFrom INT, IN vAccountIDTo INT)
	VERSION V1
	ISOLATION LEVEL CS
	RESULT SETS 1
	LANGUAGE SQL
P1: BEGIN
  DECLARE BalanceAccountDelete DECIMAL(16,4);
  
  SELECT "Balance" INTO BalanceAccountDelete FROM  "Account" WHERE "AccountID" = vAccountIDFrom;
 
  CALL TRANSACTION(BalanceAccountDelete,'DKK',vAccountIDFrom, vAccountIDTo,
  'Remaining balance to other account','Remaining balance from account to be deleted');
  
  DELETE FROM "DTUGRP09"."Account"  WHERE "AccountID"=vAccountIDFrom;
	
END P1