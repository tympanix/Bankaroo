angular.module('bankaroo').service('bankService' , ['$resource', '$http', 'localStorageService', function ($resource, $http, localStorageService) {

    var accounts = null;
    var exchanges = null;

    // Getters
    this.accounts = function () {
        return accounts;
    };

    this.exchanges = function () {
        return exchanges;
    };

    this.getAccounts = function () {
        console.log("Token:", getToken());
        var req = apiGet('/user/accounts');

        req.then(function (data) {
            accounts = data.data;
        });

        return req;
    };

    this.getHistory = function (accountId) {
        return apiGet('/user/history', {account: accountId})
    };

    this.newAccount = function (accountName, accountType, accountCurrency) {
        return apiPost('/user/new/account', {name: accountName, type: accountType, currency: accountCurrency})
    };

    this.getExchange = function () {
        var req = apiPub('/exchange');
        req.then(function (data) {
                exchanges = data.data;
            })
            .catch(function (err) {
                console.log("Error in exhanges", err);
            });
        return req;
    };

    this.getExchangeRate = function (currency) {
        var exchange = exchanges.filter(function(obj) {
            return obj.Currency == currency;
        });
        if (exchange[0]){
            return exchange[0].Rate;
        } else {
            return 100;
        }
    };

    this.getAccountTypes = function () {
        return apiPub('/accounttypes')
    };

    function apiPub(url){
        var req = {
            method: 'GET',
            url: '/api' + url
        };

        return $http(req)
    }


    function apiGet(url, params){
        var req = {
            method: 'GET',
            params: params,
            url: '/api' + url,
            headers: {
                'Authorization': getToken()
            }
        };

        return $http(req)
    }

    function apiPost(url, data){
        var req = {
            method: 'POST',
            data: data,
            url: '/api' + url,
            headers: {
                'Authorization': getToken()
            }
        };
        return $http(req)
    }

    function getToken(){
        return localStorageService.get('token');
    }


}]);