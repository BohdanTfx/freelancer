'use strict';

angular.module('FreelancerApp')
    .factory('createadminAPI', function ($http, config) {
        var urlBase = '/createadmin/',
            dataFactory = {};

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

