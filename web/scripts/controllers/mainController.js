angular.module('bankaroo').controller("mainController", ["$scope", "$http", "$location", "bankService", function($scope, $http, $location, bankService){

    $scope.accounts = null;

    $scope.apiAccounts = function () {
        bankService.apiAccounts()
    };

    $scope.apiAccounts();

    bankService.apiExchanges();

    $scope.$watch(function(){
        return bankService.accounts();
    }, function (newValue) {
        $scope.accounts = newValue;
    });

    $scope.openModal = function () {
        console.log("Open modal");
        $('.ui.modal').modal('show');
    };

    $scope.gotoDetails = function (account) {
        bankService.setSelectedAccount(account);
        $location.path('/details/' + account.AccountID)
    }

}]);