angular.module('bankaroo').directive('modal', function() {
    return {
        templateUrl: template,
        replace: true,
        transclude: true,
        scope: {
            title: '@',
            desc: '@',
            approve: '&',
            deny: '&'
        },
        restrict: 'E',
        link: link
    };

    function template(elem, attrs) {
        return attrs.templateUrl || 'some/path/default.html'
    }

    function link(scope, element, attrs) {
        //console.log("Link", element);
        $(element).modal({
            onDeny: function () {
                return scope.deny();
            },
            onApprove: function () {
                return scope.approve();
            },
            onHidden: function () {
                clearForm(element);
            }
        });
    }

    function clearForm(element){
        $(element).form('clear');
        $(element).find('.error.message').empty()
    }

});