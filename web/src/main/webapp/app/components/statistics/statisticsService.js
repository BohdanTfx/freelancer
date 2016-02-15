'use strict';

angular.module('FreelancerApp')
    .factory('statisticsAPI', function ($http, config) {
        var urlBase = '/admin/',
            dataFactory = {};

        dataFactory.getStatisticsDevCust = function () {
            return $http.get("/admin/statistics/devcust");
        };

        dataFactory.getStatisticsCreationOrders = function () {
            return $http.get("/admin/statistics/ordersCreation");
        };

        dataFactory.getStatisticPopularTests = function () {
            return $http.get("/admin/statistics/tests");
        };

        dataFactory.getStatisticOrders = function () {
            return $http.get("/admin/statistics/orders");
        };






        return dataFactory;
    });

