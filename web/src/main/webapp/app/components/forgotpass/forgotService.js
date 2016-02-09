'use strict';

angular.module('FreelancerApp')
    .factory('forgotAPI', ['$http','config', function ($http, config) {
        var dataFactory = {};

        dataFactory.confirmUserEmail = function (email) {
            var data = 'email=' + email;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/user/confirmEmail', data);
        };

    }]);