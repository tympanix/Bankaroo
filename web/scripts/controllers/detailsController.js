angular.module('bankaroo').controller("detailsController", ["$scope", "$http", "$routeParams", "$q", "bankService", function ($scope, $http, $routeParams, $q, bankService) {

    console.log("Params", $routeParams);
    $scope.accountId = $routeParams.id;

    $scope.page = "history";

    $scope.exchanges = null;
    $scope.exchange = null;
    $scope.account = null;
    $scope.timeMode = 0;

    $scope.form = {
        messageFrom: '',
        messageTo: '',
        accountTo: '',
        amount: '',
        currency: '',
        loading: false
    };

    $q.all([
        bankService.fetchAccount($scope.accountId),
        bankService.fetchAllExchanges()
    ]).then(function (data) {
        console.log("Account promise", data[0]);
        $scope.account = data[0];
        $scope.exchanges = data[1];
    });

    $scope.test = function () {
        $scope.timeMode = 1;
        $scope.exchange = 'DKK';
    };

    $scope.gotoPage = function (page) {
        $scope.page = page;
    };

    $scope.isPage = function (page) {
        if ($scope.page == page) return 'active';
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

    $scope.$on('currencyUpdate', function (event) {
        if ($scope.account == null) return;
        var exchange = $scope.account.Currency;
        console.log("Exchange", exchange);
        console.log('Dropdown event in controller', event);
        $scope.exchange = exchange;
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
    };

    $scope.showTransModal = function () {
        var isValid = $('#transactionForm').form('is valid');
        if (!isValid) {
            console.error("FORM IS NOT VALID");
            return false;
        }

        console.log("Currency", $scope.form.currency);

        $('#confirmTransModal').modal('show');
    };

    $scope.transaction = function () {
        if ($scope.form.loading) return;
        $scope.form.loading = true;

        var isValid = $('#transactionForm').form('is valid');
        if (!isValid) {
            console.error("FORM IS NOT VALID");
            return false;
        }

        bankService.apiTransaction(
            $scope.accountId,
            $scope.form.accountTo,
            $scope.form.messageFrom,
            $scope.form.messageTo,
            $scope.form.amount,
            $scope.form.currency.Currency,
            $scope.form.password)
            .then(function (data) {
                console.log("Transaction complete", data);
                $scope.getHistory();
                $('#confirmTransModal').modal('hide');
                $scope.form.messageFrom = '';
                $scope.form.messageTo = '';
                $scope.form.accountTo = '';
                $scope.form.amount = '';
                $scope.form.loading = false;
                $scope.page = 'history'
            })
            .catch(function (err) {
                console.error("Transaction error", err);
                $scope.form.loading = false;
            });

        return false;
    };

    $scope.transactionValidation = {
        inline: true,
        fields: {
            messageFrom: {
                identifier: 'messageFrom',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a message'
                    },
                    {
                        type: 'length[3]',
                        prompt: 'Your message is too short'
                    },
                    {
                        type: 'maxLength[45]',
                        prompt: 'Your message is too long'
                    }
                ]
            },

            messageTo: {
                identifier: 'messageTo',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a message'
                    },
                    {
                        type: 'length[3]',
                        prompt: 'Your message is too short'
                    },
                    {
                        type: 'maxLength[45]',
                        prompt: 'Your message is too long'
                    }
                ]
            },

            accountTo: {
                identifier: 'accountTo',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter an account number'
                    },
                    {
                        type: 'number',
                        prompt: 'The account number must be valid'
                    },
                    {
                        type: 'length[1]',
                        prompt: 'The account number is too short'
                    },
                    {
                        type: 'not[' + $scope.accountId + ']',
                        prompt: 'You can\'t send money to your own account'
                    }
                ]
            },

            amount: {
                identifier: 'amount',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please supply a currency amount'
                    },
                    {
                        type: 'integer',
                        prompt: 'Please supply a valid amount'
                    }
                ]
            }
        }
    }

}]);