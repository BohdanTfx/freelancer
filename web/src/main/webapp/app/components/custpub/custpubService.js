'use strict';

angular.module('FreelancerApp')
    .factory('custpubAPI', function ($http, config) {
        var dataFactory = {};


        dataFactory.deleteFeed = function (devId, feedId) {
            return $http.post("/user/deleteFeed?role=developer&devId=" + devId + "&feedId=" + feedId);
        };

        dataFactory.getCustById = function (id) {
            return $http.post('/user/type?role=customer&id=' + id);
        };

        dataFactory.getFeedForCust = function (id) {
            return $http.post('/user/customer/feedbacks?id=' + id);
        };

        dataFactory.getContForCust = function (id) {
            return $http.post('/user/contact?role=customer&id=' + id);
        };

        dataFactory.getRateForCust = function (id) {
            return $http.post('/user/getRateForCust?id=' + id);
        };

        dataFactory.getOrdPubHist = function (id) {
            return $http.post('/user/customer/history?custId=' + id);
        };

        dataFactory.getAvailableCustOrders = function (id) {
            return $http.post('/user/getAvailableCustOrders?id=' + id + '&from=dev');
        };

        return dataFactory;
    });