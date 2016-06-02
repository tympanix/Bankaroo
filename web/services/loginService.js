angular.module('bankaroo').service('loginService' , ['$rootScope', '$http', 'localStorageService', '$window', function ($scope, $http, localStorageService, $window) {

    this.login = function(id, password){
        var credentials = {id: id, password: password};
        $http.post('/api/login', credentials)
            .success(function (data) {
                console.log("We logged in", data);
                saveToken(data.token);
                gotoApp();
            })
            .catch(function (err) {
                console.error("Couln't log in", err)
            })
    };

    function saveToken(token){
        return localStorageService.set("token", token);
    }

    function gotoApp(){
        $window.location.href = '/bank.html';
    }

}]);
