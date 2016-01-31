/**
 * Created by Rynik on 30.01.2016.
 */
'use strict';

angular.module('FreelancerApp')
    .factory('myworksAPI', function ($http, config) {
        var urlBase = '/dev/getallworks',dataFactory = {};

        var getCustByIdPath = '/dev/getcustomerbyid';


        dataFactory.getAllWorks = function () {
            return $http.get(urlBase);
        };

        dataFactory.getCustomerById = function(customer_id){

            return $http.get(getCustByIdPath,{
                params:{cust_id : customer_id}
            });
        };



        return dataFactory;
    })
