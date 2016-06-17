angular.module('bankaroo').controller("overviewController", ["$scope", "$http", "$routeParams", "$window", "$location", "adminService", function ($scope, $http, $routeParams, $window, $location, adminService) {

    $scope.selectedCustomer = null;
    $scope.customers = null;
    $scope.accounts = null;

    // Form
    $scope.passForm = {
        password: ""
    };

    $scope.editForm = {
        cpr: "",
        name: "",
        address: "",
        zip: "",
        email: "",
        phone: ""
    };

    $scope.newForm = {
        cpr: "",
        name: "",
        address: "",
        zip: "",
        email: "",
        phone: ""
    };

    $scope.delAccountForm = {
        toSelectedAccount: null,
        toAccountNumber: '',
        select: null,
        type: null,
        accounts: getOtherAccounts
    };

    var deleteAccountIndex = -1;

    $scope.customerId = $routeParams.id;

    function getOtherAccounts() {
        if (!$scope.accounts) return null;
        if (!(deleteAccountIndex in $scope.accounts)) return null;
        return $scope.accounts.filter(function (account) {
            return account.AccountID != $scope.accounts[deleteAccountIndex].AccountID;
        })
    }

    $scope.deleteAccount = function () {
        console.log("Delete account", $scope.delAccountForm);

        $scope.delAccountForm.closing = true;
        var accountId = $scope.accounts[deleteAccountIndex].AccountID;

        var transfer;
        if ($scope.delAccountForm.select){
            transfer = $scope.delAccountForm.toSelectedAccount.AccountID;
        } else if ($scope.delAccountForm.type){
            transfer = $scope.delAccountForm.toAccountNumber;
        }

        adminService.deleteAccount(accountId, transfer)
            .then(function () {
                $scope.delAccountForm.closing = false;
                $('#deleteAccountModal').modal('hide');
                $scope.accounts.splice(deleteAccountIndex, 1);
                $scope.apiAccounts();
            })
            .catch(function () {
                $scope.delAccountForm.closing = false;
            });
        return false;
    };

    //$scope.deleteAccount = function () {
    //    var id = deleteAccountIndex;
    //    console.log("ID!!!!", id);
    //    console.log("DELETE THIS!!", $scope.accounts[id]);
    //    var accountId = $scope.accounts[id].AccountID;
    //    adminService.deleteAccount(accountId)
    //
    //        .then(function () {
    //            $scope.accounts.splice(id, 1);
    //        })
    //        .catch(function () {
    //
    //        })
    //};

    $scope.getCustomerByID = function (id) {
        $scope.loadingCustomer = true;
        adminService.getCustomerByID(id)
            .then(function (data) {
                console.log("Scope", $scope);
                console.log("Selected customer", data.data[0]);
                $scope.selectedCustomer = data.data[0];
                $scope.loadingCustomer = false;
            })
            .catch(function (err) {
                console.log("Error", err);
                $scope.loadingCustomer = false;
            })
    };

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
            phone: f.form('get value', 'phone')
        };

        adminService.newCustomer(param)
            .then(function (data) {
                console.log("Customer added", param)
            })
            .catch(function (err) {
                console.error("Customer add failed", err, param)
            })
    };

    $scope.apiAccounts = function () {
        adminService.apiAccounts($routeParams.id)
            .then(function (data) {
                console.log("Accounts", data.data);
                $scope.accounts = data.data;
            })
            .catch(function (err) {
                console.log("ACCOUNTS err", err)
            })
    };

    if ($routeParams.id) {
        $scope.getCustomerByID($routeParams.id);
        $scope.apiAccounts();
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
        var modal = $('#editUserModal');
        modal.modal('refresh');
        var isValid = $('#editUserForm').form('is valid');
        if (!isValid) {
            console.error("FORM IS NOT VALID");
            return false;
        }
        //console.log('Form', $scope.editUserForm);
        //var pristine = $scope.editUserForm.$pristine;
        //if (pristine){
        //    console.log("No changes to form");
        //    return true;
        //}

        console.log("Starting update...");
        $scope.loadingCustomer = true;
        adminService.updateUser($routeParams.id, $scope.getEditFormInputs())
            .then(function (data) {
                console.log("Scope", $scope);
                $scope.getCustomerByID($routeParams.id);
                modal.modal('hide');
            })
            .catch(function (err) {
                console.log('Update user', err);
                $('#editUserForm').form('add errors', ['Could not edit user']);
                $scope.loadingCustomer = false;
            });
    };

    $scope.showDelUserModal = function () {
        $('#deleteUserModal').modal('show');
    };

    $scope.deleteUser = function() {
        adminService.apiDeleteUser($scope.customerId)
            .then(function (data) {
                $location.path('/admin');
            })
            .catch(function () {

            })
    };

    $scope.getEditFormInputs = function () {
        var f = $('#editUserForm');

        var param = {
            name: f.form('get value', 'name'),
            address: f.form('get value', 'address'),
            zip: f.form('get value', 'zip'),
            email: f.form('get value', 'email'),
            phone: f.form('get value', 'phone')
        };

        console.log("fields", param);
        return param;
    };

    $scope.saveEditForm = function (form) {
        $scope.editUserForm = form;
    };

    $scope.saveNewUserForm = function (form) {
        $scope.newUserForm = form;
    };

    $scope.updateUser = function () {
        adminService.updateUser($scope.selectedCustomer.UserID, $scope.getEditFormInputs())
    };


    $scope.showDelAccountModal = function (id) {
        deleteAccountIndex = id;
        $scope.delAccountForm.toSelectedAccount = null;
        $scope.delAccountForm.toAccountNumber = '';
        $scope.delAccountForm.select = null;
        $scope.delAccountForm.type = null;
        $('#deleteAccountModal').modal('show');
    };

    $scope.showNewUserModal = function () {
        $('#newUserModal').modal('show');
    };

    $scope.showPassModal = function () {
        $('#changePassModal').modal('show');
    };

    $scope.showEditModal = function () {
        console.log('Showing user', $scope.selectedCustomer);
        if ($scope.selectedCustomer) {
            populateEditForm();
        }
        $('#editUserModal').modal('show');
    };

    function populateEditForm() {
        $('#editUserForm').form('set values', {
            formEditCPR: $scope.selectedCustomer.UserID,
            formEditName: $scope.selectedCustomer.UserName,
            formEditAddress: $scope.selectedCustomer.Address,
            formEditZip: $scope.selectedCustomer.PostalCode,
            formEditEmail: $scope.selectedCustomer.Email,
            formEditPhone: $scope.selectedCustomer.Phone
        });
    }

    $scope.changePassValidation = {
        inline: true,
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
            }
        }
    };

}]);