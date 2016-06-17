angular.module('bankaroo').service('adminService', ['apiService', function (api) {

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
        var req = api.post("/admin/change/password", {id: cpr, password: password});
        return req;
    };

    this.newCustomer = function (params) {
        return api.post("/admin/new/user", params)
    };

    this.searchCustomers = function (search) {
        customerSearch = search;
        var req = api.get("/admin/search/customers", {search: search});
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
        var req = api.post("/admin/update/user", params, {id: id});
        return req;
    };

    this.getCustomerByID = function (id) {
        var req = api.get("/admin/customers", {id: id});
        return req;
    };

    this.apiAccounts = function (id) {
        return api.get("/admin/accounts", {id: id})
    };

    this.deleteAccount = function (id, transfer) {
        var req = api.get("/admin/delete/account", {id: id, transfer: transfer});
        req.then(function (data) {
                console.log("Deleted account!")
            })
            .catch(function (err) {
                console.log("Error", err)
            });
        return req;
    };

    this.apiDeleteUser = function (id) {
        return api.get("/admin/delete/user", {id: id})
    };

    this.changeAccountType = function (id, type) {
        return api.get("/admin/update/account/type", {account: id, type: type})
    }



}]);