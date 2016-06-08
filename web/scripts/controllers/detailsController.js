angular.module('bankaroo').controller("detailsController", ["$scope", "$http", "$routeParams", "bankService", function($scope, $http, $routeParams, bankService){

    console.log("Params", $routeParams);
    $scope.accountId = $routeParams.id;
    $scope.bank = bankService;

    $scope.exchanges = null;
    $scope.exchange = null;
    $scope.timeMode = 0;

    $scope.get = function () {
        console.log("Customers!!!")
    };

    $scope.test = function () {
        $scope.timeMode = 1;
        $scope.exchange = 'DKK';
    };

    bankService.getExchange()
        .then(function () {
            $scope.exchange = "DKK";
        });

    // Watch bank service variables
    $scope.$watch(function(){
        return bankService.exchanges();
    }, function (newValue) {
        $scope.exchanges = newValue;
    });

    $scope.getHistory = function () {
        bankService.getHistory($scope.accountId)
            .then(function (data) {
                $scope.history = data.data;
            })
            .catch(function (err) {
                console.error("Error", err)
            })
    };

    $scope.$on('currencyUpdate', function(event) {
        console.log('Dropdown event in controller', event);
        $('#currencyDropdown').dropdown('refresh');
        $('#currencyDropdown').dropdown('set selected', 'DKK');
        $scope.exchange = 'DKK';
    });

    $scope.setExchange = function (exchange) {
        $scope.exchange = exchange;
    };

    $scope.setTimeMode = function (mode) {
        $scope.timeMode = mode;
    };

    $scope.setCurrencyDropdown = function (value) {
        $('#currencyDropdown').dropdown('set selected', value);
    };

    $scope.setTimeDropdown = function (value) {
        $('#timeDropdown').dropdown('set selected', value);
    };

    $scope.getHistory();

    $scope.labelType = function (history) {
        var type = history.TransactionType;
        if (type == 'W') return 'red';
        else if (type == 'D') return 'green';
        else return '';
    };

    $scope.sign = function (history) {
        var type = history.TransactionType;
        if (type == 'W') return '-';
        else if (type == 'D') return '+';
        else return '';
    }

}]);