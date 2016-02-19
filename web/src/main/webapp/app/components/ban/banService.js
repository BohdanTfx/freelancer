'use strict';

angular.module('FreelancerApp')
    .factory('banAPI', function ($http, config) {
        var dataFactory = {};

        dataFactory.getComplainedOrders = function (itemListStart, id) {
            /*var pagination = {};
            pagination.start = itemListStart | 0;
            //pagination.last = last;
            pagination.step = 5;

            var data = {};
            data.content = {};
            data.content.tech_id = id;
            data.page = pagination;
            return $http.post('/admin/orders/complained', data, {'Content-Type': 'application/x-www-form-urlencoded'});*/
            return $http.post('/admin/orders/complained');
        };

        dataFactory.getBanOrders = function () {
            return $http.post('/admin/orders/bans');
        };

        dataFactory.banOrder = function (orderId) {
            return $http.post('/admin/order/ban?orderId=' + orderId);
        };

        dataFactory.unbanOrder = function (orderId) {
            return $http.post('/admin/order/unban?orderId=' + orderId);
        };
        return dataFactory;
    });
