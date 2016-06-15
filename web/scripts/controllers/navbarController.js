angular.module('bankaroo').controller("navbarController", ["$scope", "$location", "loginService", function($scope, $location, loginService){

    $scope.route = function (path) {
        return ($location.path().substr(0, path.length) === path) ? 'active' : '';
    };

    $scope.isEmployee = function () {
        return loginService.userHasPermission('Employee');
    };

    $scope.main = function () {
        return ($location.path() === '/')? 'active' : '';
    }

}]);