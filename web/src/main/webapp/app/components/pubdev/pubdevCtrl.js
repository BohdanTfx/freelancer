angular.module('FreelancerApp')
    .controller('pubdevCtrl', function ($scope, pubdevAPI, $log, $http, $location, $filter) {

        $scope.query = $location.search().id;
            pubdevAPI.getDevById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.img = data.imgUrl;
                    $scope.fname = data.fname;
                    $scope.lname = data.lname;
                    $scope.hourly = data.hourly;
                    $scope.regDate = data.regDate.substring(0, 12);
                    $scope.overview = data.overview;
                }).error(function () {

                });
            pubdevAPI.getTechById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.techs = data;
                }).error(function () {

                });

            pubdevAPI.getContById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.phone = data.phone;
                    $scope.skype = data.skype;
                }).error(function () {

                });

            pubdevAPI.getPortById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.port = data;
                }).error(function () {

                });

            pubdevAPI.getRateById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.rate = data;
                }).error(function () {

                });
    });