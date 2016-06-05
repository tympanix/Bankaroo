angular.module('bankaroo').filter('currency', [function () {
    return function (amount) {
        return Number(amount).toFixed(2);
    }
}]);