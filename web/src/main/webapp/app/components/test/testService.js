'use strict';

angular.module('FreelancerApp')
    .factory('testAPI', function ($http, config) {
        var urlBase = '/dev/gettestbyid',
            dataFactory = {};

        dataFactory.getTestById = function (testId) {
            return $http.get(urlBase, {
                params: {test_id: testId}
            });
        };

        var urlResult = '/dev/results';
        dataFactory.sendAnswers = function (questions, results, testId, expireDate) {

            var data = $.param({
                results: results,
                questions: questions,
                testId: testId,
                expireDate: expireDate
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
