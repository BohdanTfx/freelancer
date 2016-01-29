'use strict';

angular.module('FreelancerApp')
    .factory('pubdevAPI', function ($http, config) {
        var urlBase = '/user/getById',
            dataFactory = {};

        dataFactory.getDevById = function (id) {
            return $http.post('/user/getById?id=' + id);
        };

        dataFactory.getTechById = function (id) {
            return $http.post('/user/getTechById?id=' + id);
        };

        dataFactory.getContById = function (id) {
            return $http.post('/user/getContById?id=' + id);
        };

        dataFactory.getPortById = function (id) {
            return $http.post('/user/getPortById?id=' + id);
        };
        dataFactory.getPortById = function (id) {
            return $http.post('/user/getPortById?id=' + id);
        };
        dataFactory.getRateById = function (id) {
            return $http.post('/user/getRate?id=' + id);
        };
        dataFactory.getFeed = function (id) {
            return $http.post('/user/getFeed?id=' + id);
        };




        return dataFactory;
    });
