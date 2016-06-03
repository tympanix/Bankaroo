$(document).ready(function () {
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
                        prompt: 'Please enter your password'
                    },
                    {
                        type: 'length[6]',
                        prompt: 'Your password must be at least 6 characters'
                    },
                    {
                        type: 'match[password]',
                        prompt: "The passwords are not the same"
                    }
                ]
            }
        }
    });
});