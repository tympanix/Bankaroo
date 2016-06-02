angular.module('bankaroo').controller("accountController", ["$scope", "$http", "$window", "bankService", function($scope, $http, $window, bankService){

    // Get exchanges
    bankService.getExchange()
        .then(function (data) {
            $scope.exchanges = data.data;
        })
        .catch(function (err) {
            console.log("Error", err)
        });

    // Get account types
    bankService.getAccountTypes()
        .then(function (data) {
           $scope.accountTypes = data.data;
        })
        .catch(function (err) {
            console.log("Error", err)
        });

    // Add new account function
    $scope.addAccount = function () {
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