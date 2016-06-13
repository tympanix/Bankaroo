angular.module('bankaroo').filter('currency', ["$injector", "bankService", function ($injector, bankService) {
    return function (amount, currency, keeplocal) {
        var $filter = $injector.get('$filter');
        var numberFilter = $filter('number');

        if (!isNumeric(amount)) amount = 0;

        var rate = bankService.getExchangeRate(currency);

        var localAmount;
        if (!keeplocal) {
            localAmount = (amount*100)/rate;
        } else {
            localAmount = amount;
        }


        var number = numberFilter(localAmount, 2);

        var localCurrency = number.toString();
        localCurrency += " ";
        localCurrency += (currency || "DKK");

        return localCurrency;

        function isNumeric(n) {
            return !isNaN(parseFloat(n)) && isFinite(n);
        }
    }
}]);