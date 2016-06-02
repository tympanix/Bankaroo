angular.module('bankaroo').controller("mainController", ["$scope", "$http", "bankService", function($scope, $http, bankService){
    $scope.getAccounts = function () {
        bankService.getAccounts()
            .then(function (data) {
                console.log("Accounts", data.data);
                $scope.accounts = data.data
            })
            .catch(function (err) {
                console.error("Error", err)
            });
    };

    $scope.getAccounts();
}]);