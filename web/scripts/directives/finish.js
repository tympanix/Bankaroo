angular.module('bankaroo').directive('onFinish', ["$timeout", function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    console.log('Repeat event', attr["onFinish"]);
                    scope.$emit('currencyUpdate');
                });
            }
        }
    }
}]);