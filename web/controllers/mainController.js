angular.module('bankaroo').controller("mainController", ["$scope", "$http", "bankService", function($scope, $http, bankService){

    $scope.accounts = null;

    $scope.getAccounts = function () {
        bankService.getAccounts()
    };

    $scope.getAccounts();

    $scope.$watch(function(){
        return bankService.accounts();
    }, function (newValue) {
        $scope.accounts = newValue;
    });

    $scope.openModal = function () {
        console.log("Open modal");
        $('.ui.modal').modal('show');
    }

}]);