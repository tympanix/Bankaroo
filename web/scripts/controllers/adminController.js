angular.module('bankaroo').controller("adminController", ["$scope", "$http", "$routeParams", "$window", "adminService", function ($scope, $http, $routeParams, $window, adminService) {

    // Customer search form
    $scope.customerSearch = adminService.customerSearch();
    $scope.selectedCustomer = null;
    $scope.searching = false;

    // Form
    $scope.passFormPassword = "";

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
        adminService.getCustomerByID(id)
            .then(function (data) {
                console.log("Selected customer", data.data[0]);
                $scope.selectedCustomer = data.data[0];
            })
            .catch(function (err) {
                console.log("Error", err)
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

    function deleteAccount(){
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
    }

    $scope.deleteAccount = function (deleteId) {
        var id = $scope.deleteAccountIndex;
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
        adminService.changePassword($scope.selectedCustomer.UserID, $scope.passFormPassword)
            .then(function (data) {
                console.log("PASS WAS CHANGED");
                modal.modal('hide');
            })
            .catch(function (err) {
                $('#changePassForm').form('add errors', ['Could not change password'])
            });
    };

    $scope.showPassModal = function () {
        $('#changePassModal').modal('show');
    };

    $scope.initPassModal = function () {
        $('#changePassModal').modal({
            onDeny: function () {
                console.log("NO!");
                return true;
            },
            onApprove: function () {
                //$scope.formChangePass();
                return false;
            }
        });
    };

    $scope.initValidation = function () {
        console.log("UPDATE FORM VALIDATION!");
        $('#changePassForm').form({
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
        });
    };

    $scope.showDelAccountModal = function (id) {
        deleteAccountIndex = id;
        console.log("Delete Index", deleteAccountIndex);
        console.log("Accounts", $scope.accounts);
        $('#deleteAccountModal').modal('show');
        $scope.initDelAccountModal();
    };

    $scope.initDelAccountModal = function () {
        $('#deleteAccountModal').modal({
            onDeny: function () {
                console.log("NO!");
                return true;
            },
            onApprove: function () {
                deleteAccount();
                return true;
            }
        });
    }

}]);