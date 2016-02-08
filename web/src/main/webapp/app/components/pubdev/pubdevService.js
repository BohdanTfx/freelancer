'use strict';

angular.module('FreelancerApp')
    .factory('pubdevAPI', function ($http, config) {
        var urlBase = '/user/getById',
            dataFactory = {};

        dataFactory.getAvailableCustOrders = function (id) {
            return $http.post('/user/getAvailableCustOrders?id=' + id);
        };

        dataFactory.getCustById = function (id) {
            return $http.post('/user/type?role=customer&id=' + id);
        };

        dataFactory.getDevById = function (id) {
            return $http.post('/user/type?role=dev&id=' + id);
        };

        dataFactory.deleteFeed = function (custId, feedId) {
            return $http.post("/user/deleteFeed?role=customer&custId=" + custId + "&feedId=" + feedId);
        };

        dataFactory.getTechById = function (id) {
            return $http.post('/user/getTechById?id=' + id);
        };

        dataFactory.getContById = function (id) {
            return $http.post('/user/getContById?id=' + id);
        };

        dataFactory.getFeedById = function (id) {
            return $http.post('/user/getFeed?id=' + id);
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
        dataFactory.getTestByDevId = function (id) {
            return $http.post('/user/getTestByDevId?id=' + id);
        };

        return dataFactory;
    });