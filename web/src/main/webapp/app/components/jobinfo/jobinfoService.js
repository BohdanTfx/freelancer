'use strict';

angular.module('FreelancerApp')
    .factory('orderAPI', function ($http, config) {
        var urlBase = 'user/orders/getorderbyid?orderId=',
            dataFactory = {};

        dataFactory.getOrderById = function (orderId) {
            return $http.post(urlBase + orderId);
        };

        var urlFollowers = 'user/orders/getfollowersbyorderid?orderId=';
        dataFactory.getFollowers = function (orderId) {
            return $http.post(urlFollowers + orderId);
        };

        var urlCust = 'user/orders/getcustomerbyid?custId=';

        dataFactory.getCustomerById = function (customerId) {
            return $http.post(urlCust + customerId);
        };

        var urlFeedbacks = 'user/orders/getcustomerfeedbacks?custId=';

        dataFactory.getCustomerFeedbacks = function (customerId) {
            return $http.post(urlFeedbacks + customerId);
        };

        var urlTechs = 'user/orders/getordertechs';

        dataFactory.getOrderTechs = function (orderId) {
            return $http.post(urlTechs + orderId);
        };

        return dataFactory;
    });
