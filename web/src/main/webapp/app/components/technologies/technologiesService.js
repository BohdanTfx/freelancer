/**
 * Created by Rynik on 19.02.2016.
 */
'use strict';

angular.module('FreelancerApp')
    .factory('technologiesAPI', function ($http, config) {
        var dataFactory = {};

        dataFactory.addTechnology = function (name) {
            return $http.post('/admin/add/technology?name='+name);
        };

        dataFactory.deleteTechnology = function (id) {
            return $http.post('/admin/delete/technology?id='+id);
        };




        return dataFactory;
    });
