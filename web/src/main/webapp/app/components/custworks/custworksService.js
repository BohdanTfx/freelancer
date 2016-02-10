/**
 * Created by Rynik on 30.01.2016.
 */
'use strict';

angular.module('FreelancerApp')
    .factory('custworksAPI', function ($http, config) {
        var urlBase = '/custworks',dataFactory = {};


        dataFactory.getAllWorks = function () {
            return $http.post("/cust/allWorks");
        };

        dataFactory.getFollowers = function (orderId) {
            return $http.post("/user/order/followers?orderId=" + orderId);
        };


        return dataFactory;
    })
/**
 * Created by Rynik on 10.02.2016.
 */
