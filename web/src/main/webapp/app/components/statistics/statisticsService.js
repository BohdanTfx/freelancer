'use strict';

angular.module('FreelancerApp')
    .factory('statisticsAPI', function ($http, config) {
        var urlBase = '/admin/',
            dataFactory = {};

        dataFactory.getStatisticsDevCust = function () {
            return $http.get("/admin/statistics/devcust");
        };

        dataFactory.getStatisticsCreationOrders = function () {
            return $http.get("/admin/statistics/orders");
        };

        dataFactory.getStatisticPopularTests = function () {
            return $http.get("/admin/statistics/tests");
        };






        return dataFactory;
    });

