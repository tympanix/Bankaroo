angular.module('bankaroo').service('adminService' , ['$resource', '$http', 'localStorageService', function ($resource, $http, localStorageService) {

    this.getCustomers = function (name) {
        return apiGet("/admin/customers", {name: name})
    };

    this.getAccounts = function (id) {
        return apiGet("/admin/accounts", {id: id})
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