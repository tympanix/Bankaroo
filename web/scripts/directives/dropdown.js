angular.module('bankaroo').directive('dropdown', function() {
    return {
        scope: {
            selected: '=',
            onChange: '&'
        },
        restrict: 'A',
        link: link
    };

    function link(scope, element, attrs) {
        $(element).dropdown();

        $(element).dropdown({
            onChange: function (value, text, choice) {
                scope.onChange(value, text, choice);
                console.log("Dropwdown change");
                console.log("Value", value);
                console.log("Text", text);
                console.log("Choice", choice);
            }
        });

        console.log("Dropdown init", scope.selected);
        $(element).dropdown('set selected', String(scope.selected));

        scope.$watch(function() {return scope.selected; }, function(newValue){
            console.log("Dropdown select", newValue);
            $(element).dropdown('set selected', String(newValue));
        });
    }

});