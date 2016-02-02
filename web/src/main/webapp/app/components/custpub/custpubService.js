'use strict';

angular.module('FreelancerApp')
    .factory('pubdevAPI', function ($http, config) {
        var urlBase = '/cust/',
            dataFactory = {};


        dataFactory.getCustById = function (id) {
            return $http.post('/cust/getCustById?id=' + id);
        };

        dataFactory.getFeedForCust = function (id) {
            return $http.post('/cust/getFeedForCust?id=' + id);
        };


        return dataFactory;
    });