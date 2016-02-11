/**
 * Created by Rynik on 30.01.2016.
 */
'use strict';

angular.module('FreelancerApp')
    .factory('myworksAPI', function ($http, config) {
        var urlBase = '/myworks',dataFactory = {};

        dataFactory.getAllDeveloperWorks = function () {
            return $http.get("/dev/works/all");
        };

        dataFactory.getCustomerById = function(customer_id){
            return $http.get("/dev/customerById?customer_id="+customer_id);
        };

        dataFactory.getDevWorkersByIdOrder = function (order_id){
            return $http.get('/dev/workersByIdOrder?order_id='+order_id);
        };

        dataFactory.getCustWorkersByIdOrder = function (order_id){
            return $http.get('/cust/workersByIdOrder?order_id='+order_id);
        };

        dataFactory.getAllCustomerWorks = function () {
            return $http.post("/cust/allWorks");
        };

        //
        //dataFactory.getFollowers = function (orderId) {
        //    return $http.post("/user/order/followers?orderId=" + orderId);
        //};




        return dataFactory;
    })
