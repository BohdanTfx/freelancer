'use strict';

angular.module('FreelancerApp')
    .factory('custpubAPI', function ($http, config) {
        var urlBase = '/cust/',
            dataFactory = {};


        dataFactory.getCustById = function (id) {
            return $http.post('/cust/getCustById?id=' + id);
        };

        dataFactory.getFeedForCust = function (id) {
            return $http.post('/cust/getFeedForCust?id=' + id);
        };

        dataFactory.getContForCust = function (id) {
            return $http.post('/cust/getContForCust?id=' + id);
        };

        dataFactory.getRateForCust = function (id) {
            return $http.post('/cust/getRateForCust?id=' + id);
        };

        dataFactory.getOrdPubHist = function (id) {
            return $http.post('/user/orders/getcustomerhistory?custId=' + id);
        };

        dataFactory.getAvailableCustOrders = function (id) {
            return $http.post('/cust/getAvailableCustOrders?id=' + id + '&from=dev');
        };

        return dataFactory;
    });