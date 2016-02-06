'use strict';

angular.module('FreelancerApp')
    .factory('adminAPI', function ($http, config) {
        var urlBase = '/admin/',
            dataFactory = {};

        dataFactory.getStatisticsDevCust = function () {
            return $http.get("/admin/statistics");
        };


        dataFactory.sendLinkToEmail = function (email) {
            return $http.post('/admin/create/new/admin?email=' + email);
        };

        dataFactory.checkAvailableUUID = function (uuid) {
            return $http.post('/admin/check/uuid?uuid=' + uuid);
        };

        dataFactory.removeUUID = function (uuid) {
            return $http.post('/admin/remove/uuid?uuid=' + uuid);
        };



        return dataFactory;
    });

