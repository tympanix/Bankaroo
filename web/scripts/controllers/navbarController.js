angular.module('bankaroo').controller("navbarController", ["$scope", "$location", function($scope, $location){

    $scope.route = function (path) {
        return ($location.path().substr(0, path.length) === path) ? 'active' : '';
    };

    $scope.main = function () {
        return ($location.path() === '/')? 'active' : '';
    }

}]);