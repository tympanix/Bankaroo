angular.module('bankaroo').service('adminService', ['$resource', '$http', 'localStorageService', function ($resource, $http, localStorageService) {

    var selectedCustomer = {};
    var customerSearch = "";
    var customers = null;

    // Getters
    this.customers = function () {
        return customers;
    };

    this.selectedCustomer = function (customer) {
        if (customer) selectedCustomer = customer;
        else return selectedCustomer;
    };

    this.customerSearch = function () {
        return customerSearch;
    };

    this.changePassword = function (cpr, password) {
        var req = apiPost("/admin/change/password", {id: cpr, password: password});
        return req;
    };

    this.newCustomer = function (params) {
        return apiPost("/admin/new/user", params)
    };

    this.getCustomers = function (name) {
        customerSearch = name;
        var req = apiGet("/admin/customers", {name: name});
        req.then(function (data) {
                console.log("Customers!", data.data);
                customers = data.data;
            })
            .catch(function (err) {
                console.log("Error", err)
            });
        return req;
    };

    this.updateUser = function (id, params) {
        var req = apiPost("/admin/update/user", params, {id: id});
        return req;
    };

    this.getCustomerByID = function (id) {
        var req = apiGet("/admin/customers", {id: id});
        return req;
    };

    this.getAccounts = function (id) {
        return apiGet("/admin/accounts", {id: id})
    };

    this.deleteAccount = function (id) {
        var req = apiGet("/admin/delete/account", {id: id});
        req.then(function (data) {
                console.log("Deleted account!")
            })
            .catch(function (err) {
                console.log("Error", err)
            });
        return req;
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

    function apiPost(url, data, params) {
        var req = {
            method: 'POST',
            data: data,
            params: params,
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