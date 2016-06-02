// Module
var bankaroo = angular.module("bankaroo", ["ngRoute", "ngResource", "LocalStorageModule"]);

// Routes
bankaroo.config(function($routeProvider){

    $routeProvider
        .when("/", {
            templateUrl: "views/main.html",
            controller: "mainController"
        })
        .when("/details/:id", {
            templateUrl: "/views/details.html",
            controller: "detailsController"
        })
        .when("/new/account", {
            templateUrl: "views/account.html",
            controller: "accountController"
        })
});

bankaroo.config(function (localStorageServiceProvider) {
    localStorageServiceProvider
        .setPrefix('bankaroo')
        .setStorageType('sessionStorage')
        .setNotify(true, true)
});


