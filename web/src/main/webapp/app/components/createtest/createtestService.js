'use strict';

angular.module('FreelancerApp')
    .factory('createtestAPI', function ($http, config) {
        var dataFactory = {};

        dataFactory.getAllTests = function () {
            return $http.get('/admin/tests');
        };

        dataFactory.getQuestionsByTechId = function (id) {
            return $http.get('/admin/tech/questions?id=' + id);
        };

        dataFactory.getAllTechnologies= function () {
            return $http.get('/admin/technologies');
        };

        return dataFactory;
    });
