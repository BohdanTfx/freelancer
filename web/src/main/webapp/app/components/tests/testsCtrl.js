angular.module('FreelancerApp')
    .controller('testsCtrl', function ($scope, testsAPI, $log) {

        testsAPI.getAllTests().success(function(data){
            $log.log(data);
            $scope.devQAs = data.devQAs;
            $scope.tests= data.tests;
        }).error(function () {
            alert(404);
        });

        $scope.sortField = undefined;
        $scope.reverse = false;

        $scope.sort = function(fieldName){
            if($scope.sortField === fieldName){
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
    });