angular.module('bankaroo').service('adminService', ['$resource', '$http', 'localStorageService', function ($resource, $http, localStorageService) {

    var customerSearch = "";
    var customers = [];

    // Getters
    this.customers = function () {
        return customers;
    };

    this.customerSearch = function () {
        return customerSearch;
    };

    this.getCustomers = function (name) {
        customerSearch = name;
        var req = apiGet("/admin/customers", {name: name})
        req.then(function (data) {
                console.log("Customers!", data.data);
                customers = data.data;
            })
            .catch(function (err) {
                console.log("Error", err)
            });
        return req;
    };

    this.getAccounts = function (id) {
        return apiGet("/admin/accounts", {id: id})
    };

    function apiPub(url) {
        var req = {
            method: 'GET',
            url: '/api' + url
        };

        return $http(req)
    }


    function apiGet(url, params) {
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

    function apiPost(url, data) {
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

    function getToken() {
        return localStorageService.get('token');
    }


}]);