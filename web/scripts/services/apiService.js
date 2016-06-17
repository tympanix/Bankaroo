angular.module('bankaroo').service('apiService', ['$http', "$q", "loginService", function ($http, $q, loginService) {

    const BASE = '/Bankaroo';

    /*Returns a promise that the requested element will be found in the array
     * by retrieving it directly or loading it from the external api call*/
    function $fetch(find, array, compare, apiCall){
        return $q(function (resolve, reject) {
            var skip = false;
            find(array, compare)
                .then(function (found) {
                    //console.log("Retrieved directly", found);
                    skip = true;
                    return $q.when(found)
                })
                .catch(function (err) {
                    //console.log("Not found directly", err);
                    return apiCall()
                })
                .then(function (data) {
                    if (skip) return $q.when(data);
                    //console.log("Retrieve by API");
                    return find(data.data, compare)
                })
                .then(function (found) {
                    resolve(found)
                })
                .catch(function (err) {
                    //console.log("Retrieve failed from API");
                    reject(err)
                })
        });
    }

    function asyncFind(array, compare){
        return $q(function (resolve, reject) {
            if (!array){
                reject('Array is null/undefined');
            } else {
                var found = array.find(compare);
                if (found !== undefined){
                    //console.log("Retrieve async find", found);
                    resolve(found);
                } else {
                    //console.log("Retrieve async find: no result");
                    reject("Array item not found")
                }
            }
        })
    }

    function asyncArray(array){
        return $q(function (resolve, reject) {
            if (!array) reject('Array is null/undefined');
            else resolve(array);
        })
    }

    this.$fetchElement = function(array, compare, apiCall) {
        return $fetch(asyncFind, array, compare, apiCall);
    };

    this.$fetchArray = function(array, apiCall){
        return $fetch(asyncArray, array, null, apiCall)
    };

    this.get = function(url, params){
        var req = {
            method: 'GET',
            params: params,
            url: BASE + '/api' + url,
            headers: {
                'Authorization': loginService.getToken()
            }
        };

        return $http(req)
    };

    this.post = function(url, data, params){
        var req = {
            method: 'POST',
            params: params,
            data: data,
            url: BASE + '/api' + url,
            headers: {
                'Authorization': loginService.getToken()
            }
        };
        return $http(req)
    };

}]);
