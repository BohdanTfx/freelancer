'use strict';

angular.module('FreelancerApp')
    .factory('testsAPI', function ($http, config) {
        var urlBase = '/dev/getalltests',
            dataFactory = {};

        dataFactory.getAllTests = function () {
            return $http.get(urlBase);
        };


        return dataFactory;
    });
