angular.module('bankaroo').controller("accountController", ["$scope", "$http", "$window", "bankService", function($scope, $http, $window, bankService){


    $scope.show = function () {
        console.log("Name:", $scope.accountName);
        console.log("Type:", $scope.accountType);
        console.log("Currency:", $scope.accountCurrency);
        bankService.newAccount($scope.accountName, $scope.accountType, $scope.accountCurrency)
            .then(function () {
                $window.location.href = '#/';
            })
            .catch(function (err) {
                console.log("Error new account", err)
            })
    }
}]);