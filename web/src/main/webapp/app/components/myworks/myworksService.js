/**
 * Created by Rynik on 30.01.2016.
 */
'use strict';

angular.module('FreelancerApp')
    .factory('myworksAPI', function ($http, config) {
        var urlBase = '/dev/getallworks',dataFactory = {};
        var getCustById = '/dev/getcustomerbyid';
        var getWorkersByIdOrder = '/dev/getworkersbyidorder';


        dataFactory.getAllWorks = function () {
            return $http.get(urlBase);
        };

        dataFactory.getCustomerById = function(customer_id){
            return $http.get(getCustById,{
                params:{cust_id : customer_id}
            });
        };

        dataFactory.getWorkersByIdOrder = function (order_id){
            return $http.get(getWorkersByIdOrder,{
                params:{order_id:order_id}
            })
        }



        return dataFactory;
    })
