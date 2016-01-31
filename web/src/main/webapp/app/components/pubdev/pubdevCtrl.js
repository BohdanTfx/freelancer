angular.module('FreelancerApp')
    .controller('pubdevCtrl', function ($scope, pubdevAPI, $log, $http, $location, $filter) {

        $scope.query = $location.search().id;

        var getCust = function (cust_id) {
            pubdevAPI.getCustById(cust_id).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.cust = data;
                }).error(function () {

                });
        };

        pubdevAPI.getDevById($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.id = data.id;
                    $scope.img = data.imgUrl;
                    $scope.email = data.email;
                    $scope.fname = data.fname;
                    $scope.lname = data.lname;
                    $scope.hourly = data.hourly;
                    $scope.regDate = data.regDate.substring(0, 12);
                    $scope.overview = data.overview;
                    $scope.position = data.position;
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
                    $scope.ports = data;
                }).error(function () {

                });

            pubdevAPI.getRateById($scope.query).success(
                function (data, status, headers, config) {
                    $scope.rate = data;
                }).error(function () {

                });

        pubdevAPI.getFeed($scope.query).success(
            function (data, status, headers, config) {
                console.log(data);
                $scope.feeds = data;
            }).error(function () {

            });

        $scope.send = function () {
            var data = 'message=' + $scope.mes + '&email=' + $scope.email;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/send', data).success(function () {
                $scope.messuc = 'Message sent successfully.';
            }).error(function () {
                $scope.messerr = 'Error, while sending message.';
            });
        };

        $scope.comment = function () {
            var data = 'comment=' + $scope.com + '&id=' + $scope.id + '&rate=' + $scope.comrate;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/comment', data).success(function () {
                $scope.comsuc = 'Comment sent successfully.';
            }).error(function () {
                $scope.comerr = 'Error, bad value.';
            });
        };

        $scope.getNumber = function (count) {

            var ratings = [];

            for (var i = 0; i < count; i++) {
                ratings.push(i)
            }

            return ratings;
        }
    });