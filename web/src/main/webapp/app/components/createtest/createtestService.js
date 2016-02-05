'use strict';

angular.module('FreelancerApp')
    .factory('createtestAPI', function ($http, config) {
        var urlTests = '/admin/tests',
            dataFactory = {};

        dataFactory.getAllTests = function () {
            return $http.get(urlBase);
        };

        dataFactory.getAllQuestions = function () {
            return $http.post('/admin/questions');
        };

        dataFactory.getAllTechnologies= function (customerId) {
            return $http.post('/user/orders/tech');
        };

        return dataFactory;
    });
