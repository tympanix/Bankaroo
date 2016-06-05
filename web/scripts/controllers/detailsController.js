angular.module('bankaroo').controller("detailsController", ["$scope", "$http", "$routeParams", "bankService", function($scope, $http, $routeParams, bankService){

    console.log("Params", $routeParams);
    $scope.accountId = $routeParams.id;
    $scope.bank = bankService;
    $scope.hello = "Hello";

    $scope.get = function () {
        console.log("Customers!!!")
    };

    $scope.getHistory = function () {
        bankService.getHistory($scope.accountId)
            .then(function (data) {
                $scope.history = data.data;
            })
            .catch(function (err) {
                console.error("Error", err)
            })
    };

    $scope.getHistory();

    $scope.showModal = function () {
        $('.ui.modal').modal('show');
    }

}]);