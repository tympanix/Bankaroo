angular.module('bankaroo').service('bankService' , ['$resource', '$http', '$q', 'apiService', function ($resource, $http, $q, api) {

    var accounts = null;
    var exchanges = null;
    var selectedAccount = null;
    var user = null;

    // Getters & Setters
    this.accounts = function () {
        return accounts;
    };

    this.user = function () {
        if (!user) return null;
        if (0 in user) return user[0];
    };

    this.exchanges = function () {
        return exchanges;
    };

    this.getSelectedAccount = function () {
        return selectedAccount;
    };

    this.setSelectedAccount = function (account) {
        selectedAccount = account;
    };

    function accountById(id){
        return function (account) {
            return account.AccountID == id;
        }
    }

    function exchangeByCurrency(currency){
        return function (currency) {
            return currency.Currency == currency;
        }
    }

    this.apiAccounts = function () {
        var req = api.get('/user/accounts');
        req.then(function (data) {
            accounts = data.data;
        }).catch(function (err) {
            console.error("Accounts", err)
        });

        return req;
    };

    this.fetchAccount = function (id) {
        return api.$fetchElement(accounts, accountById(id), this.apiAccounts);
    };

    this.fetchExchange = function (currency) {
        return api.$fetchElement(exchanges, exchangeByCurrency(currency), this.apiExchanges)
    };

    this.fetchAllExchanges = function () {
        return api.$fetchArray(exchanges, this.apiExchanges);
    };

    this.fetchUser = function () {
        return api.$fetchElement(user, function () {
            return true; // Always first element
        }, this.apiUser)
    };

    this.fetchAccounts = function () {
        return api.$fetchArray(accounts, this.apiAccounts)
    };

    this.getHistory = function (accountId) {
        return api.get('/user/history', {account: accountId})
    };

    this.apiUser = function () {
        var req = api.get("/user/user");
        req.then(function (data) {
                user = data.data
            })
            .catch(function (err) {
                console.error("Get user details", err);
            });
        return req;
    };

    this.newAccount = function (accountName, accountType, accountCurrency) {
        return api.post('/user/new/account', {name: accountName, type: accountType, currency: accountCurrency})
    };

    this.apiExchanges = function () {
        var req = api.get('/exchange');
        req.then(function (data) {
                exchanges = data.data;
            })
            .catch(function (err) {
                console.log("Error in exhanges", err);
            });
        return req;
    };

    this.getExchangeRate = function (currency) {
        if (!exchanges) return 0;
        var exchange = exchanges.filter(function(obj) {
            return obj.Currency == currency;
        });
        if (exchange[0]){
            return exchange[0].Rate;
        } else {
            return 100;
        }
    };

    this.apiTransaction = function (accountFrom, accountTo, messageFrom, messageTo, amount, currency, password) {
        var param = {
            accountFrom: accountFrom,
            accountTo: accountTo,
            messageFrom: messageFrom,
            messageTo: messageTo,
            amount: amount,
            currency: currency,
            password: password
        };
        return api.post('/user/transaction', param)
    };

    this.getAccountTypes = function () {
        return api.get('/accounttypes')
    };

    this.apiCloseAccount = function (closeId, transferId) {
        return api.get("/user/close/account", {account: closeId, transfer: transferId})
    };

}]);