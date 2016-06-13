angular.module('bankaroo').service('bankService' , ['$resource', '$http', '$q', 'localStorageService', function ($resource, $http, $q, localStorageService) {

    const BASE = '/Bankaroo';
    var accounts = [];
    var exchanges = [];
    var selectedAccount = [];
    var user = [];

    // Getters & Setters
    this.accounts = function () {
        return accounts;
    };

    this.user = function () {
        return user[0];
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

    function asyncFind(array, compare){
        return $q(function (resolve, reject) {
            var found = array.find(compare);
            if (found !== undefined){
                console.log("Retrieve async find", found);
                resolve(found);
            } else {
                console.log("Retrieve async find: no result");
                reject("Array item not found")
            }
        })
    }

    function asyncArray(array){
        return $q(function (resolve, reject) {
            if (array === undefined) reject();
            else if (array.length == 0) reject();
            else resolve(array);
        })
    }

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

    function userById(id){
        return function (user) {
            return user.UserId == id;
        }
    }

    this.apiAccounts = function () {
        var req = apiGet('/user/accounts');
        req.then(function (data) {
            accounts = data.data;
        }).catch(function (err) {
            console.error("Accounts", err)
        });

        return req;
    };


    this.fetchAccount = function (id) {
        return $fetchElement(accounts, accountById(id), this.apiAccounts);
    };

    this.fetchExchange = function (currency) {
        return $fetchElement(exchanges, exchangeByCurrency(currency), this.apiExchanges)
    };

    this.fetchAllExchanges = function () {
        return $fetchArray(exchanges, this.apiExchanges);
    };

    this.fetchUser = function () {
        return $fetchElement(user, function () {
            return true; // Always first element
        }, this.apiUser)
    };

    this.fetchAccounts = function () {
        return $fetchArray(accounts, this.apiAccounts)
    };


    /*Returns a promise that the requested element will be found in the array
     * by retrieving it directly or loading it from the external api call*/
    function $fetch(find, array, compare, apiCall){
        return $q(function (resolve, reject) {
            var skip = false;
            find(array, compare)
                .then(function (found) {
                    console.log("Retrieved directly", found);
                    skip = true;
                    return $q.when(found)
                })
                .catch(function (err) {
                    console.log("Not found directly", err);
                    return apiCall()
                })
                .then(function (data) {
                    if (skip) return $q.when(data);
                    console.log("Retrieve by API");
                    return find(data.data, compare)
                })
                .then(function (found) {
                    resolve(found)
                })
                .catch(function (err) {
                    console.log("Retrieve failed from API");
                    reject(err)
                })
        });
    }

    function $fetchElement(array, compare, apiCall) {
        return $fetch(asyncFind, array, compare, apiCall);
    }

    function $fetchArray(array, apiCall){
        return $fetch(asyncArray, array, null, apiCall)
    }

    this.getHistory = function (accountId) {
        return apiGet('/user/history', {account: accountId})
    };

    this.apiUser = function () {
        var req = apiGet("/user/user");
        req.then(function (data) {
                user = data.data
            })
            .catch(function (err) {
                console.error("Get user details", err);
            });
        return req;
    };

    this.newAccount = function (accountName, accountType, accountCurrency) {
        return apiPost('/user/new/account', {name: accountName, type: accountType, currency: accountCurrency})
    };

    this.apiExchanges = function () {
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
        return apiPost('/user/transaction', param)
    };

    this.getAccountTypes = function () {
        return apiPub('/accounttypes')
    };

    function apiPub(url){
        var req = {
            method: 'GET',
            url: BASE + '/api' + url
        };

        return $http(req)
    }


    function apiGet(url, params){
        var req = {
            method: 'GET',
            params: params,
            url: BASE + '/api' + url,
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
            url: BASE + '/api' + url,
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