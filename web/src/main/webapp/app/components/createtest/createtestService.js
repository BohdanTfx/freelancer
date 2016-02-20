'use strict';

angular.module('FreelancerApp')
    .factory('createtestAPI', function ($http, config) {
        var dataFactory = {};

        dataFactory.getAllTests = function () {
            return $http.get('/admin/tests');
        };

        dataFactory.getAllQuestions = function () {
            return $http.get('/admin/questions');
        };



        dataFactory.getQuestionsByTechId = function (itemListStart, id, last) {
            var pagination = {};
            pagination.start = itemListStart | 0;
            pagination.last = last;
            pagination.step = 5;

            var data = {};
            data.content = {};
            data.content.tech_id = id;
            data.page = pagination;
            return $http.post('/admin/tech/questions', data, {'Content-Type': 'application/x-www-form-urlencoded'});
        };

        dataFactory.getAllTechnologies = function () {
            return $http.get('/admin/technologies');
        };

        dataFactory.createTest = function (testJSON, questionsJSON) {
            return $http.post('/admin/test/create?test=' + testJSON + "&questions=" + questionsJSON);
        };

        dataFactory.createQuestion = function (questionJSON, answersJSON) {
            return $http.post('/admin/question?question=' + questionJSON+ "&answers=" + answersJSON);
        };

        dataFactory.deleteTest = function(testJSON) {
            return $http.post('/admin/test/delete?test=' + testJSON);
        };

        dataFactory.deleteQuestion = function(questionJSON) {
            return $http.post('/admin/question/delete?question=' + questionJSON);
        };

        return dataFactory;
    });
