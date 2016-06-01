// Module
var bankaroo = angular.module("bankaroo", ["ngRoute", "ngResource"]);

// Routes
bankaroo.config(function($routeProvider){

    $routeProvider
        .when("/", {
            templateUrl: "/views/details.html",
            controller: "detailsController"
        })
        .when("/accounts", {
            templateUrl: "views/accounts.html",
            controller: "serversController"
        })
});
