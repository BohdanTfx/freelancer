'use strict';

angular.module('FreelancerApp')
    .factory('orderAPI', function ($http, config) {
        var urlBase = 'user/order?orderId=',
            dataFactory = {};

        dataFactory.getOrderById = function (orderId) {
            return $http.post(urlBase + orderId);
        };

        var urlFollowers = 'user/order/followers?orderId=';
        dataFactory.getFollowers = function (orderId) {
            return $http.post(urlFollowers + orderId);
        };

        var urlCust = 'cust/getCustById?id=';

        dataFactory.getCustomerById = function (customerId) {
            return $http.post(urlCust + customerId);
        };

        var urlFeedbacks = 'cust/getFeedForCust?id=';

        dataFactory.getCustomerFeedbacks = function (customerId) {
            return $http.post(urlFeedbacks + customerId);
        };

        var urlTechs = 'user/order/techs?orderId=';

        dataFactory.getOrderTechs = function (orderId) {
            return $http.post(urlTechs + orderId);
        };

        var urlCustHistory = 'cust/history?custId=';
        dataFactory.getCustomerHistory = function (custId) {
            return $http.post(urlCustHistory+ custId);
        };

        var urlDevRate = '/user/getRate?id=';
        dataFactory.getRateById = function (id) {
            return $http.post(urlDevRate + id);
        };

        var urlSubscribe = '/user/order/subscribe?';
        dataFactory.subscribe = function (message, orderId) {
            return $http.post(urlSubscribe + 'message=' + message + '&orderId=' + orderId);
        };
        return dataFactory;
    });
