angular.module('FreelancerApp')
    .controller('createtestCtrl', function ($scope, createtestAPI, Notification, $log) {
        $scope.availableQuestions = [];

        $scope.chosenQuestions = [];

        $scope.openPage = function (page) {
            if (page == 'last')
                $scope.itemListStart = 'last';
            else {
                $scope.itemListStart = page;
                $scope.getQuestionsByTechId($scope.chosenTechID);
            }
        }

        $scope.fillPagination = function (data) {
            $scope.pages = data;

            for (var page = 0; page < data.length; page++) {
                var item = data[page];
                if (item.first == 'current') {
                    if (item.second > 3)
                        $scope.showFirst = true;
                    else
                        $scope.showFirst = false;
                    if (item.second + 4 >= $scope.maxPage)
                        $scope.showLast = false;
                    else
                        $scope.showLast = true;
                }
            }
        }

        $scope.getQuestionsByTechId = function (id) {
            if (id != $scope.chosenTechID) {
                $scope.chosenQuestions = [];
            }
            $scope.chosenTechID = id;
            createtestAPI.getQuestionsByTechId($scope.itemListStart, id).success(function (data) {
                $scope.questions = data.items;
                $scope.availableQuestions = $scope.questions;
                $scope.availableQuestions.size = $scope.availableQuestions.length;

                for (var i = 0; i < $scope.chosenQuestions.length; i++) {
                    for (var j = 0; j < $scope.availableQuestions.length; j++) {
                        if ($scope.chosenQuestions[i].id == $scope.availableQuestions[j].id) {
                            $scope.availableQuestions[j].checked = true;
                            break;
                        }
                    }
                }

                $scope.fillPagination(data.pages);
            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Something went bad. Please try again.'
                    });
            });
        }

        createtestAPI.getAllTests().success(function (data) {
            $scope.tests = data;
        }).error(function () {
            Notification
                .error({
                    title: 'Error!',
                    message: 'Something went bad. Please try again.'
                });
        });

        createtestAPI.getAllTechnologies().success(function (data) {
            $scope.technologies = data;
        }).error(function () {
            Notification
                .error({
                    title: 'Error!',
                    message: 'Something went bad. Please try again.'
                });
        });

        $scope.createTest = function () {
            if ($scope.chosenQuestions.length < 3) {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Min count of questions is 3. Please add questions in test.'
                    });
            } else {
                var testJSON = JSON.stringify($scope.test);
                var qIDs = [];
                for (var i = 0; i < $scope.chosenQuestions.length; i++) {
                    qIDs.push($scope.chosenQuestions[i].id);
                }
                var questionsJSON = JSON.stringify(qIDs);

                createtestAPI.createTest(testJSON, questionsJSON).success(function (data) {
                    $scope.test.id = data;
                    $scope.testReset();
                    Notification
                        .success({
                            title: 'Success!',
                            message: 'Test was successfully created.'
                        });
                }).error(function () {
                    Notification
                        .error({
                            title: 'Error!',
                            message: 'Something went bad. Please try again.'
                        });
                });
            }
        }

        $scope.moveRight = function (id) {
            for (var i = 0; i < $scope.availableQuestions.length; i++) {
                if ($scope.availableQuestions[i].id == id) {
                    $scope.chosenQuestions.push($scope.availableQuestions[i]);
                    $scope.availableQuestions[i].checked = true;
                    $scope.availableQuestions.size--;
                }
            }
        }

        $scope.moveLeft = function (id) {
            for (var i = 0; i < $scope.chosenQuestions.length; i++) {
                if ($scope.chosenQuestions[i].id == id) {
                    for (var k = 0; k < $scope.availableQuestions.length; k++) {
                        if ($scope.availableQuestions[k].id == id) {
                            $scope.availableQuestions[k].checked = false;
                            break;
                        }
                    }
                    $scope.availableQuestions.size++;
                    $scope.chosenQuestions.splice(i, 1);
                }
            }
        }


        $scope.testReset = function () {
            $scope.test = {};
            $scope.pages = [];
            $scope.chosenQuestions = [];
            $scope.availableQuestions = [];
            $scope.showLast = false;
        }

        var question = {};
        var answer = {name: '', correct: false};
        $scope.answers = [];
        $scope.answers.push(Object.assign({}, answer));

        $scope.addAnswerForm = function () {
            $scope.answers.push(Object.assign({}, answer));
        }

        $scope.deleteAnswerForm = function (id) {
            $scope.answers.splice(id, 1);
        }

        $scope.createQuestion = function () {
            if ($scope.answers.length < 2) {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Min count of answers is 2. Please add answers in question.'
                    });
            } else {
                var questionJSON = JSON.stringify($scope.question);
                var answersJSON = JSON.stringify($scope.answers);

                createtestAPI.createQuestion(questionJSON, answersJSON).success(function (data) {
                    $scope.question.id = data;
                    $scope.questionReset();
                    $scope.getQuestionsByTechId($scope.chosenTechID);
                    Notification
                        .success({
                            title: 'Success!',
                            message: 'Question was successfully created.'
                        });
                }).error(function () {
                    Notification
                        .error({
                            title: 'Error!',
                            message: 'Something went bad. Please try again.'
                        });
                });
            }
        }

        $scope.questionReset = function () {
            $scope.answers = [];
            $scope.answers.push(Object.assign({}, answer));
            $scope.question = {};
        }
    });