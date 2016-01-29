angular.module('FreelancerApp')

    .controller('testCtrl', function ($scope, testAPI, $stateParams, $log, $interval) {

        $scope.testFinished = false;
        $scope.hideCssClass = '';

        $scope.intervalCtrl = function () {
            var self = this, j = 0, counter = 0;
            self.mode = 'query';
            self.activated = true;
            self.determinateValue = 0;
            self.modes = [];
            /**
             * Turn off or on the 5 themed loaders
             */
            self.toggleActivation = function () {
                if (self.activated) {
                    j = counter = 0;
                    self.determinateValue = 0;
                }
            };

            var time = $scope.test.secPerQuest * $scope.test.questions.length;
            var intervalTimeUpdate = 2;//time * 10;
            var intervalProccess = $interval(function () {
                self.determinateValue += 1;
                if (self.determinateValue > 100) {
                    $interval.cancel(intervalProccess);
                    $scope.submitAnswers();
                }
            }, intervalTimeUpdate, 0, true);

            $scope.vm = self;
        }


        testAPI.getTestById($stateParams.testId).success(function (data) {
            $log.log(data);
            $scope.test = data;
            $scope.intervalCtrl();
        }).error(function () {
            alert(404);
        });


        $scope.redirect = function () {
            window.location = "http://localhost:8080/#/tests";
        };

        $scope.submitAnswers = function () {
            var results = [];
            var questions = $scope.test.questions;
            for (var i = 0; i < questions.length; i++) {
                var q = {};
                q.questionId = questions[i].id;
                q.answersId = [];
                var answers = questions[i].answers;
                for (var j = 0; j < answers.length; j++) {
                    if (answers[j].check) {
                        q.answersId.push(answers[j].id);
                    }
                }
                results.push(q);
            }

            var jsonResults = JSON.stringify(results);
            var jsonQuests = JSON.stringify(questions);

            testAPI.sendAnswers(jsonQuests, jsonResults).success(function (data) {
                $log.log(data);
                $scope.result = data;
                $scope.testFinished = true;
                $scope.hideCssClass = '.ng-hide';
            }).error(function () {
                alert("Server is busy");
            });
        }
    });