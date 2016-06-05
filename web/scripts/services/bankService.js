angular.module('bankaroo').service('bankService' , ['$resource', '$http', 'localStorageService', function ($resource, $http, localStorageService) {

    var accounts = null;

    // Getters
    this.accounts = function () {
        return accounts;
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
        return apiPub('/exchange')
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