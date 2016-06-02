angular.module('bankaroo').controller("mainController", ["$scope", "$http", "bankService", function($scope, $http, bankService){
    $scope.getAccounts = function () {
        bankService.getAccounts()
    };

    $scope.getAccounts();

    $scope.$watch(function(){
        return bankService.accounts();
    }, function (newValue) {
        $scope.accounts = newValue;
    });

}]);