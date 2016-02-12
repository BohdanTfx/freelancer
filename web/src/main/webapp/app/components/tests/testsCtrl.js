angular.module('FreelancerApp')
    .controller('testsCtrl', function ($scope, testsAPI, $log, $interval, Notification) {

        $scope.smt = {};
        $scope.smt.passedTest = false;
        $scope.smt.allowedTest = false;


        testsAPI.getAllTests().success(function (data) {
            $scope.devQAs = data.devQAs;
            $scope.tests = data.tests;
            if (data.tests.length == 0)
                $scope.noTest = true;
            else
                $scope.noTest = false;

            $scope.testsDivision();
        }).error(function () {
            Notification
                .error({
                    title: $translate.instant('notification.error'),
                    message: $translate.instant('notification.smth-wrong')
                });
        });

        $scope.sortField = undefined;
        $scope.reverse = false;

        $scope.sort = function (fieldName) {
            if ($scope.sortField === fieldName) {
                $scope.reverse = !$scope.reverse;
            } else {
                $scope.sortField = fieldName;
                $scope.reverse = false;
            }
        };

        $scope.isSortUp = function (fieldName) {
            return $scope.sortField === fieldName && !$scope.reverse;
        };
        $scope.isSortDown = function (fieldName) {
            return $scope.sortField === fieldName && $scope.reverse;
        };

        $scope.testsDivision = function (tests) {
            var passedTests = [];
            var newTests = [];
            for (var i = 0; i < $scope.tests.length; i++) {
                var p = false;
                for (var j = 0; j < $scope.devQAs.length; j++) {
                    if ($scope.tests[i].id == $scope.devQAs[j].testId) {
                        if ($scope.devQAs[j].isExpire == false) {
                            $scope.tests[i].repass = new Date($scope.devQAs[j].expire) - new Date;
                        }
                        p = true;
                        break;
                    }
                }
                if (p) {
                    if ($scope.devQAs[j].isExpire)
                        $scope.tests[i].status = 'expired';
                    else {
                        $scope.tests[i].status = 'passed';
                    }
                    passedTests.push($scope.tests[i]);
                } else {
                    $scope.tests[i].status = 'new';
                    newTests.push($scope.tests[i]);
                }
            }
            $scope.passedTestList = passedTests;
            $scope.newTestsList = newTests;
        }

        $scope.testListCtrl = function () {
            if ($scope.smt.passedTest == $scope.smt.allowedTest) {
                $scope.tests = $scope.passedTestList.concat($scope.newTestsList);
            } else if ($scope.smt.passedTest == true) {
                $scope.tests = $scope.passedTestList;
            } else {
                $scope.tests = $scope.newTestsList;
            }
        }
    });