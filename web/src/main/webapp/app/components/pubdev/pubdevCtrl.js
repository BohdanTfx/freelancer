angular.module('FreelancerApp')
    .controller('pubdevCtrl', function ($scope, pubdevAPI, $log, $http, $location) {

        $scope.query = $location.search().id;

        $scope.get = function () {
            pubdevAPI.getDevById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                }).error(function () {

                });
            pubdevAPI.getTechById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                }).error(function () {

                });

            pubdevAPI.getContById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                }).error(function () {

                });

            pubdevAPI.getPortById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                }).error(function () {

                });

            pubdevAPI.getRateById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                }).error(function () {

                });

        }
    });