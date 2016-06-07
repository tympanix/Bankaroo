angular.module('bankaroo').filter('time', ["bankService", function (bankService) {
    return function (time, mode) {
        // Subtract one hour (because of time differences)
        var localtime = moment.utc(time).subtract(1, "hour").local();

        switch (mode){
            case 0: return localtime.fromNow();
            case 1: return localtime.format("DD/MM/YYYY HH:mm");
            default: return localtime.fromNow();
        }

    }
}]);