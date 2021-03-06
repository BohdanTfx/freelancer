'use strict';

angular.module('FreelancerApp')
    .factory('orderAPI', function ($http, config) {
        var urlBase = '/user/order?orderId=',
            dataFactory = {};

        dataFactory.getOrderById = function (orderId) {
            return $http.post(urlBase + orderId);
        };

        dataFactory.toComplain = function (orderId) {
            return $http.post("/user/orders/complain?orderID=" + orderId);
        };

        dataFactory.isCompAlrEx = function (orderId) {
            return $http.post("/user/orders/isCompAlrEx?orderId=" + orderId);
        };


        var urlFollowers = '/user/order/followers?orderId=';
        dataFactory.getFollowers = function (orderId) {
            return $http.post(urlFollowers + orderId);
        };

        var urlCust = '/user/type?role=customer&id=';

        dataFactory.getCustomerById = function (customerId) {
            return $http.post(urlCust + customerId);
        };

        var urlContact = '/user/contact?role=customer&id=';

        dataFactory.getCustomerContactById = function (customerId) {
            return $http.post(urlContact + customerId);
        };

        var urlFeedbacks = '/user/customer/feedbacks?id=';

        dataFactory.getCustomerFeedbacks = function (customerId) {
            return $http.post(urlFeedbacks + customerId);
        };

        var urlTechs = '/user/order/techs?orderId=';

        dataFactory.getOrderTechs = function (orderId) {
            return $http.post(urlTechs + orderId);
        };

        var urlCustHistory = '/user/customer/history?custId=';
        dataFactory.getCustomerHistory = function (custId) {
            return $http.post(urlCustHistory + custId);
        };

        var urlDevRate = '/user/getRate?id=';
        dataFactory.getRateById = function (id) {
            return $http.post(urlDevRate + id);
        };

        var urlSubscribe = '/user/order/subscribe?';
        dataFactory.subscribe = function (message, orderId) {
            return $http.post(urlSubscribe + 'message=' + message + '&orderId=' + orderId);
        };

        dataFactory.acceptFollower = function (devId, jobId, jobName, customer, acceptDate) {
            return $http.post('/cust/dev/accept?' + 'devId=' + devId +
                '&jobId=' + jobId + '&jobName=' + jobName +
                '&customer=' + customer + '&acceptDate=' + acceptDate);
        };

        dataFactory.isWorker = function (follower) {
            return $http.get('/cust/dev/isWorker?follower=' + follower);
        };

        dataFactory.banOrder = function (orderId) {
            return $http.post('/admin/order/ban?orderId=' + orderId);
        };

        return dataFactory;
    });