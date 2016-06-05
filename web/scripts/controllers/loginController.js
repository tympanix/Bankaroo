angular.module('bankaroo').controller("loginController", ["$scope", "$http", "loginService", function($scope, $http, loginService){

    $scope.login = function () {
        loginService.login($scope.id, $scope.pass);
    };

    $scope.hello = "Hello there!";

}]);