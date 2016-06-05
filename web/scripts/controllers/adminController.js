angular.module('bankaroo').controller("adminController", ["$scope", "$http", "$routeParams", "$window", "adminService", function ($scope, $http, $routeParams, $window, adminService) {

    // Customer search form
    $scope.customerSearch = adminService.customerSearch();
    $scope.selectedCustomer = null;
    $scope.searching = false;

    // Form
    $scope.passForm = {
        password: ""
    };

    $scope.editForm = {
        name: "",
        address: "",
        zip: "",
        email: "",
        phone: ""
    };

    var deleteAccountIndex = -1;

    $scope.customerId = $routeParams.id;

    $scope.customers = null;
    $scope.accounts = null;

    $scope.getCustomers = function () {
        if ($scope.searching) return;
        $scope.searching = true;
        console.log("Changed!", $scope.customerSearch);
        adminService.getCustomers($scope.customerSearch)
            .then(function () {
                $scope.searching = false;
            });
        adminService.customerSearch($scope.customerSearch);
    };

    $scope.getCustomerByID = function (id) {
        $scope.loadingCustomer = true;
        adminService.getCustomerByID(id)
            .then(function (data) {
                console.log("Selected customer", data.data[0]);
                $scope.selectedCustomer = data.data[0];
                $scope.loadingCustomer = false;
            })
            .catch(function (err) {
                console.log("Error", err);
                $scope.loadingCustomer = false;
            })
    };

    $scope.$watch(function () {
        return adminService.customers();
    }, function (newValue) {
        $scope.customers = newValue;
    });

    $scope.getAccounts = function () {
        adminService.getAccounts($routeParams.id)
            .then(function (data) {
                console.log("Accounts", data.data);
                $scope.accounts = data.data;
            })
            .catch(function (err) {
                console.log("ACCOUNTS err", err)
            })
    };

    $scope.gotoCustomer = function (id) {
        var customer = $scope.customers[id];
        adminService.selectedCustomer(customer);
        $window.location.href = '#/admin/' + customer.UserID;
    };

    $scope.deleteAccount = function () {
        var id = deleteAccountIndex;
        console.log("ID!!!!", id);
        console.log("DELETE THIS!!", $scope.accounts[id]);
        var accountId = $scope.accounts[id].AccountID;
        adminService.deleteAccount(accountId)

            .then(function () {
                $scope.accounts.splice(id, 1);
            })
            .catch(function () {

            })
    };

    if ($routeParams.id) {
        $scope.getCustomerByID($routeParams.id);
        $scope.getAccounts();
    }

    $scope.formChangePass = function () {
        var modal = $('#changePassModal');
        modal.modal('refresh');
        var isValid = $('#changePassForm').form('is valid');
        if (!isValid) {
            console.error("FORM IS NOT VALID");
            return false;
        }
        console.log("CHANGE PASS!");
        adminService.changePassword($scope.selectedCustomer.UserID, $scope.passForm.password)
            .then(function (data) {
                console.log("PASS WAS CHANGED");
                modal.modal('hide');
            })
            .catch(function (err) {
                $('#changePassForm').form('add errors', ['Could not change password'])
            });
    };

    $scope.formEditUser = function () {
        $scope.loadingCustomer = true;
        var modal = $('#editUserModal');
        modal.modal('refresh');
        var isValid = $('#editUserForm').form('is valid');
        if (!isValid) {
            console.error("FORM IS NOT VALID");
            return false;
        }
        adminService.updateUser($scope.selectedCustomer.UserID, $scope.getEditFormInputs())
            .then(function (data) {
                $scope.getCustomerByID($routeParams.id);
                modal.modal('hide');
            })
            .catch(function (err) {
                $('#editUserForm').form('add errors', ['Could not edit user'])
            });

    };

    $scope.getEditFormInputs = function () {
        console.log("Form", $scope.editUserForm);
        var f = $scope.editUserForm;
        var fields = {};
        if (f.name.$dirty) fields.name = $scope.editForm.name;
        if (f.address.$dirty) fields.address = $scope.editForm.address;
        if (f.zip.$dirty) fields.zip = $scope.editForm.zip;
        if (f.email.$dirty) fields.email = $scope.editForm.email;
        if (f.phone.$dirty) fields.phone = $scope.editForm.phone;

        console.log("fields", fields);
        return fields;
    };

    $scope.saveForm = function (form) {
        $scope.editUserForm = form;
    };

    $scope.updateUser = function () {
        adminService.updateUser($scope.selectedCustomer.UserID, $scope.getEditFormInputs())
    };


    $scope.showDelAccountModal = function (id) {
        deleteAccountIndex = id;
        $('#deleteAccountModal').modal('show');
    };

    $scope.showPassModal = function () {
        $('#changePassModal').modal('show');
    };

    $scope.showEditModal = function () {
        console.log('Showing user', $scope.selectedCustomer.UserName);
        $('#editUserForm').form('set values', {
            formEditName: $scope.selectedCustomer.UserName,
            formEditAddress: $scope.selectedCustomer.Address,
            formEditZip: $scope.selectedCustomer.PostalCode,
            formEditEmail: $scope.selectedCustomer.Email,
            formEditPhone: $scope.selectedCustomer.Phone
        });
        $('#editUserModal').modal('show');
    };

    $scope.changePassValidation = {
        fields: {
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

            passwordRep: {
                identifier: 'password_rep',
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

    $scope.editFormValidation = {
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
            }
        }
    };

}]);