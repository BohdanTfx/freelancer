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
            var delta = 100 / time;

            $scope.time = time;

            var timerCtrl = $interval(function () {
                $scope.time -= 1;
                self.determinateValue += delta;
                console.log(self.determinateValue );
                $scope.timer = ""+ ("0" + parseInt($scope.time/60, 10)).slice(-2)+":" + ("0" + $scope.time%60).slice(-2);
                if ($scope.time == 0) {
                    $interval.cancel(timerCtrl);
                    $scope.submitAnswers();
                }
            }, 1000, 0, true);

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