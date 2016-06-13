angular.module('bankaroo').directive('modal', function() {
    return {
        templateUrl: template,
        replace: true,
        transclude: true,
        scope: {
            title: '@',
            btnOk: '@',
            btnCancel: '@',
            icon: '@',
            approve: '&',
            deny: '&',
            data: '='
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

        scope.$on("$destroy", function() {
            element.remove();
            console.info("Removed modal", element);
        });
    }

    function clearForm(element){
        $(element).form('clear');
        $(element).find('.error.message').empty()
    }

});