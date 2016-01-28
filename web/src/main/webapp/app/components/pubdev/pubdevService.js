'use strict';

angular.module('FreelancerApp')
    .factory('pubdevAPI', function ($http, config) {
        var urlBase = '',
            dataFactory = {};

        dataFactory.getAll = function () {
            return $http.get(urlBase);
        };


        return dataFactory;
    });
