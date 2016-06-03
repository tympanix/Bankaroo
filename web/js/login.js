$(document).ready(function () {
    $('.ui.form').form({
        fields: {
            number: {
                identifier: 'number',
                rules: [
                    {
                        type: 'empty',
                        prompt: 'You CPR number is required'
                    },
                    {
                        type: 'number',
                        prompt: 'Please enter a number'
                    },
                    {
                        type: "length[6]",
                        prompt: "You CPR number is too short"
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
            }
        }
    });
});