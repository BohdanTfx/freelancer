'use strict';

angular.module('FreelancerApp')
    .factory('testAPI', function ($http, config) {
        var urlBase = '/dev/gettestbyid',
            dataFactory = {};

        dataFactory.getTestById = function (testId) {
            return $http.get(urlBase, {
                params: { test_id: testId }
            });
        };

        var urlResult = '/dev/getresults';
        dataFactory.sendAnswers = function (questions, results) {

            var data = $.param({
                results: results,
                questions: questions
            });

            return $http({
                method: 'POST',
                url: urlResult,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            data: data
            });
        };
        return dataFactory;
    });