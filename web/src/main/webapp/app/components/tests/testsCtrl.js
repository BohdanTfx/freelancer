angular.module('FreelancerApp')
    .controller('testsCtrl', function ($scope, testsAPI, $log, $interval) {

        $scope.smt = {};

        testsAPI.getAllTests().success(function (data) {
            $log.log(data);
            $scope.devQAs = data.devQAs;
            $scope.tests = data.tests;
            $scope.testsDivision();
        }).error(function () {
            alert(404);
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
                        if($scope.devQAs[j].isExpire == false){
                            $scope.tests[i].repass = new Date($scope.devQAs[j].expire) - new Date;
                        }
                        p = true;
                        break;
                    }
                }
                if (p) {
                    if ($scope.devQAs[j].isExpire)
                        $scope.tests[i].status = 'expired';
                    else{
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
            console.log("a: " + $scope.smt.allowedTest + " p:" +$scope.smt.passedTest);
            if ($scope.smt.passedTest == $scope.smt.allowedTest) {
                $scope.tests = $scope.passedTestList.concat($scope.newTestsList);
                console.log(1);
            } else if ($scope.smt.passedTest == true) {
                $scope.tests = $scope.passedTestList;
                console.log(2);
            } else {
                $scope.tests = $scope.newTestsList;
                console.log(3);
            }
        }
    });