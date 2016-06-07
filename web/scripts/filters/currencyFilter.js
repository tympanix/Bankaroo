angular.module('bankaroo').filter('currency', ["$injector", "bankService", function ($injector, bankService) {
    return function (amount, currency) {
        var $filter = $injector.get('$filter');
        var numberFilter = $filter('number');

        var rate = bankService.getExchangeRate(currency);
        var localAmount = (amount*100)/rate;

        var number = numberFilter(localAmount, 2);

        var localCurrency = number.toString();
        localCurrency += " ";
        localCurrency += (currency || "DKK");

        return localCurrency;
    }
}]);