'use strict';

angular.module('FreelancerApp')
    .factory('adminAPI', function ($http, config) {
        var urlBase = '/admin/',
            dataFactory = {};


        dataFactory.createAdmin = function (email) {
            return $http.post('/admin/create/new/admin?email=' + email);
        };

        dataFactory.checkAvailableUUID = function (uuid) {
            return $http.post('/admin/check/uuid?uuid=' + uuid);
        };



        return dataFactory;
    });

