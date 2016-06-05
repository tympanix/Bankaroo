angular.module('bankaroo').directive('validation', function() {
    return {
        scope: {
            validation: '=',
            ngSubmit: '&'
        },
        restrict: 'A',
        compile: function compile(element, tAttrs, transclude) {
            return {
                post: function(scope, element, attrs) {
                    $(element).form(scope.validation);
                }
            }
        }
    };
});