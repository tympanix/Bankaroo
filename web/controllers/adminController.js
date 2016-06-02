angular.module('bankaroo').controller("adminController", ["$scope", "$http", "$routeParams", "$window", "adminService", function($scope, $http, $routeParams, $window, adminService){

    // Customer search form
    $scope.customerSearch = "";

    $scope.customerId = $routeParams.id;

    $scope.customers = [];
    $scope.accounts = [];

    $scope.getCustomers = function () {
        console.log("Changed!", $scope.customerSearch);
        adminService.getCustomers($scope.customerSearch)
            .then(function (data) {
                $scope.customers = data.data;
                console.log("UPDATE!", data.data)
            })
            .catch(function (err) {
                console.log("Error", err)
            })
    };

    $scope.getAccounts = function () {
        adminService.getAccounts($routeParams.id)
            .then(function (data) {
                console.log("Accounts", data.data);
                $scope.accounts = data.data;
            })
            .catch(function (err) {
                console.log("ACCOUNTS err", err)
            })
    };

    $scope.gotoCustomer = function (id) {
        $window.location.href = '#/admin/' + id;
    };


    $scope.getCustomers();

    if ($routeParams.id){
        $scope.getAccounts();
    }

}]);