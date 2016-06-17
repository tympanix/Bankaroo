angular.module('bankaroo').controller("accountController", ["$scope", "$http", "$window", "bankService", function ($scope, $http, $window, bankService) {

    // Form parameters
    $scope.accountName = null;
    $scope.accountType = null;
    $scope.accountCurrency = null;

    // Get exchanges
    bankService.apiExchanges();

    // Get account types
    bankService.getAccountTypes()
        .then(function (data) {
            $scope.accountTypes = data.data;
        })
        .catch(function (err) {
            console.log("Error", err)
        });

    // Watch bank service variables
    $scope.$watch(function(){
        return bankService.exchanges();
    }, function (newValue) {
        $scope.exchanges = newValue;
    });

    // Add new account function
    $scope.addAccount = function () {
        var f = $('#newAccountForm');

        if (!f.form('is valid')){
            console.error("New account form is invalid!");
            return false;
        }

        console.log("Name:", $scope.accountName);
        console.log("Type:", $scope.accountType);
        console.log("Currency:", $scope.accountCurrency);
        bankService.newAccount($scope.accountName, $scope.accountType, $scope.accountCurrency)
            .then(function () {
                bankService.apiAccounts();
                $window.location.href = '#/';
            })
            .catch(function (err) {
                console.log("Error new account", err)
            })
    };

    $scope.newAccountValidation = {
        inline: true,
        fields: {
            name: {
                identifier: 'name',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter an account name'
                    },
                    {
                        type: 'length[2]',
                        prompt: 'You account name is too short'
                    }
                ]
            },

            type: {
                identifier: 'type',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please select an account type'
                    }
                ]
            },

            currency: {
                identifier: 'currency',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please select an currency type'
                    }
                ]
            }
        }
    };

}]);