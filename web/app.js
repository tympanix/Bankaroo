// Module
var bankaroo = angular.module("bankaroo", ["ngRoute", "ngResource", "LocalStorageModule"]);

// Routes
bankaroo.config(function($routeProvider){

    $routeProvider
        .when("/", {
            templateUrl: "./views/main.html",
            controller: "mainController"
        })
        .when("/details/:id", {
            templateUrl: "./views/details.html",
            controller: "detailsController"
        })
        .when("/new/account", {
            templateUrl: "./views/account.html",
            controller: "accountController"
        })
        .when("/admin", {
            templateUrl: "./views/admin.html",
            controller: "adminController"
        })
        .when("/admin/:id", {
            templateUrl: "./views/overview.html",
            controller: "overviewController"
        })
});

bankaroo.config(function (localStorageServiceProvider) {
    localStorageServiceProvider
        .setPrefix('bankaroo')
        .setStorageType('sessionStorage')
        .setNotify(true, true)
});


