angular.module('bankaroo').controller("adminController", ["$scope", "$http", "$routeParams", "$window", "adminService", function ($scope, $http, $routeParams, $window, adminService) {

    // Customer search form
    $scope.customerSearch = adminService.customerSearch();
    $scope.customers = null;
    $scope.searching = false;

    $scope.newForm = {
        cpr: "",
        name: "",
        address: "",
        zip: "",
        email: "",
        phone: ""
    };

    $scope.searchCustomers = function () {
        if ($scope.searching) return;
        $scope.searching = true;
        console.log("Changed!", $scope.customerSearch);
        adminService.searchCustomers($scope.customerSearch)
            .then(function () {
                $scope.searching = false;
            });
        //adminService.customerSearch($scope.customerSearch);
    };

    $scope.$watch(function () {
        return adminService.customers();
    }, function (newValue) {
        $scope.customers = newValue;
    });

    $scope.newCustomer = function () {
        var f = $('#newUserForm');
        var isValid = f.form('is valid');
        if (!isValid) {
            console.error("FORM IS NOT VALID");
            return false;
        }

        var param = {
            cpr: f.form('get value', 'cpr'),
            name: f.form('get value', 'name'),
            address: f.form('get value', 'address'),
            zip: f.form('get value', 'zip'),
            email: f.form('get value', 'email'),
            phone: f.form('get value', 'phone'),
            password: f.form('get value', 'password')
        };

        adminService.newCustomer(param)
            .then(function (data) {
                console.log("Customer added", param)
            })
            .catch(function (err) {
                console.error("Customer add failed", err, param)
            })
    };

    $scope.gotoCustomer = function (id) {
        var customer = $scope.customers[id];
        adminService.selectedCustomer(customer);
        $window.location.href = '#/admin/' + customer.UserID;
    };

    $scope.saveNewUserForm = function (form) {
        $scope.newUserForm = form;
    };

    $scope.showNewUserModal = function () {
        $('#newUserModal').modal('show');
    };

    $scope.newCustomerValidation = {
        inline: true,
        fields: {
            name: {
                identifier: 'name',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a name'
                    },
                    {
                        type: 'length[2]',
                        prompt: 'You name is too short'
                    }
                ]
            },

            cpr: {
                identifier: 'cpr',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a CPR Number'
                    },
                    {
                        type: 'number',
                        prompt: "Please enter a valid CPR Number"
                    },
                    {
                        type: 'length[10]',
                        prompt: "Your CPR Number is too short"
                    }
                ]
            },

            address: {
                identifier: 'address',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter an address'
                    },
                    {
                        type: 'length[4]',
                        prompt: "Your address is too short"
                    }
                ]
            },

            zip: {
                identifier: 'zip',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a zip code'
                    },
                    {
                        type: 'length[4]',
                        prompt: "Your zip code is too short"
                    }
                ]
            },

            email: {
                identifier: 'email',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter an email'
                    },
                    {
                        type: 'email',
                        prompt: "Please enter a valid email"
                    }
                ]
            },

            phone: {
                identifier: 'phone',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter a phone number'
                    },
                    {
                        type: 'number',
                        prompt: "Please enter a valid phone number"
                    },
                    {
                        type: 'length[8]',
                        prompt: "Your phone number is too short"
                    }
                ]
            },

            password: {
                identifier: 'password',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please enter your password'
                    },
                    {
                        type: 'length[6]',
                        prompt: 'Your password must be at least 6 characters'
                    }
                ]
            },

            repPassword: {
                identifier: 'repPassword',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'Please repeat your password'
                    },
                    {
                        type: 'match[password]',
                        prompt: "The passwords does not match"
                    }
                ]
            }
        }
    };

}]);