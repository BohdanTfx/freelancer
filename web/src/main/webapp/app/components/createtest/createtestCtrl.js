angular.module('FreelancerApp')
    .controller('createtestCtrl', function ($scope, createtestAPI, $log) {
        $scope.availableQuestions = [];

        $scope.chosenQuestions = [];

        createtestAPI.getQuestionsByTechId(id).success(function (data) {
            $scope.questions = data;
            $scope.availableQuestions = $scope.questions;
        }).error(function () {
            alert(404);
        });

        createtestAPI.getAllTests().success(function (data) {
            $scope.tests = data;
        }).error(function () {
            alert(404);
        });

        createtestAPI.getAllTechnologies().success(function (data) {
            $scope.technologies = data;
        }).error(function () {
            alert(404);
        });
        $scope.createTest = function () {

        }

        $scope.moveRight = function (id) {
            for (var i = 0; i < $scope.availableQuestions.length; i++) {
                if ($scope.availableQuestions[i].id == id) {
                    $scope.chosenQuestions.push($scope.availableQuestions[i]);
                    $scope.availableQuestions.splice(i, 1);
                }
            }
        }

        $scope.moveLeft = function (id) {
            for (var i = 0; i < $scope.chosenQuestions.length; i++) {
                if ($scope.chosenQuestions[i].id == id) {
                    $scope.availableQuestions.push($scope.chosenQuestions[i]);
                    $scope.chosenQuestions.splice(i, 1);
                }
            }
        }


    });