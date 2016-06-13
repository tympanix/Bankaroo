angular.module('bankaroo').controller("mainController", ["$scope", "$http", "$location", "$q", "bankService", function($scope, $http, $location, $q, bankService){

    $scope.accounts = null;
    $scope.user = null;
    $scope.ready = false;

    $q.all([
        bankService.fetchUser(),
        bankService.fetchAccounts(),
        bankService.fetchAllExchanges()
    ]).then(function (data) {
        console.log("Main promise", data);
        $scope.user = data[0];
        $scope.accounts = data[1];
        $scope.ready = true;
    });

    bankService.apiExchanges();

    $scope.$watch(function(){
        return bankService.accounts();
    }, function (newValue) {
        $scope.accounts = newValue;
    });

    $scope.$watch(function(){
        return bankService.user();
    }, function (newValue) {
        $scope.user = newValue;
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