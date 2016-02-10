'use strict';

angular.module('FreelancerApp')
    .factory('forgotAPI', ['$http','config', function ($http, config) {
        var dataFactory = {};

        dataFactory.confirmUserEmail = function (email) {
            var data = 'email=' + email;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/user/confirmEmail', data);
        };

        dataFactory.confirmPhoneCode = function (email, phoneCode){
            var data = 'email=' + email + '&phoneCode=' + phoneCode;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/user/confirmPhoneCode', data);
        };

        dataFactory.changePassword = function (email, password){
            var data = 'email=' + email + '&password=' + password;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/user/changePassword', data);
        };

        return dataFactory;

    }]);