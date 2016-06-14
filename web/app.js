// Module
var bankaroo = angular.module("bankaroo", ["ngRoute", "ngResource", "LocalStorageModule", "angular-jwt"]);

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
            controller: "adminController",
            permission: "Employee"
        })
        .when("/admin/:id", {
            templateUrl: "./views/overview.html",
            controller: "overviewController",
            permission: "Employee"
        })
});

bankaroo.config(function (localStorageServiceProvider) {
    localStorageServiceProvider
        .setPrefix('bankaroo')
        .setStorageType('sessionStorage')
        .setNotify(true, true)
});

bankaroo.run(["$rootScope", "$location", "$window", "loginService", function ($rootScope, $location, $window, loginService) {
    $rootScope.$on('$routeChangeStart', function (event, next) {

        console.log("NEXT!", next);
        console.log("NEW LOCATION!", next.permission);

        if (next.permission){
            if (!loginService.userHasPermission(next.permission)){
                console.log("RESTRICTED!");
                $location.path('/');
                //$window.location.href = './login.html';
            }
        }

        //if (!userAuthenticated && !next.isLogin) {
        //    /* You can save the user's location to take him back to the same page after he has logged-in */
        //    $rootScope.savedLocation = $location.url();
        //
        //    $location.path('/User/LoginUser');
        //}
    });

}]);


