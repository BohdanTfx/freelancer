angular.module('FreelancerApp')
    .controller('pubdevCtrl', function ($scope, pubdevAPI, $log, $http, $location, $filter, $stateParams) {
        //console.log($stateParams.devName, )

        $scope.query = $location.search().id;

        var getCust = function (cust_id) {
            pubdevAPI.getCustById(cust_id).success(
                function (data, status, headers, config) {
                    console.log(data);
                    $scope.cust = data;
                }).error(function () {

                });
        };

        $scope.empty = function (obj) {
            for (var prop in obj) {
                if (obj.hasOwnProperty(prop))
                    return false;
            }

            return true;
        };

        pubdevAPI.getDevById($scope.query).success(
            function (data, status, headers, config) {
                console.log(data + ' dev');
                $scope.id = data.id;
                if (typeof data.imgUrl == 'undefined')
                    $scope.img = 'http://placehold.it/350x150';
                else
                    $scope.img = data.imgUrl;
                $scope.email = data.email;
                $scope.fname = data.fname;
                $scope.lname = data.lname;
                $scope.hourly = data.hourly;
                $scope.regDate = data.regDate.substring(0, 12);
                $scope.overview = data.overview;
                $scope.position = data.position;

            }).error(function () {
                $scope.freelancerNotFound = true;
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
                if (data.length !== 0)
                    $scope.ports = data;
                else
                    $scope.emptyPort = true;
            }).error(function () {
                $scope.emptyPort = true;
            });

        pubdevAPI.getRateById($scope.query).success(
            function (data, status, headers, config) {
                $scope.rate = data;
            }).error(function () {

            });

        pubdevAPI.getFeed($scope.query).success(
            function (data, status, headers, config) {
                console.log(data);
                if (data.length !== 0) {
                    $scope.feeds = data;

                    for (var i = 0; i < $scope.feeds.length; i++) {
                        if (typeof $scope.feeds[i].customer.imgUrl == 'undefined')
                            $scope.feeds[i].customer.imgUrl = 'http://placehold.it/350x150';
                    }
                }
                else
                    $scope.emptyComm = true;
            }).error(function () {
                $scope.emptyComm = true;
            });

        $scope.sendSms = function () {
            $scope.sms = 'sms';
            $scope.phone = "380932497376";

            var data = 'phone=' + $scope.phone + '&sms=' + $scope.sms;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/sms', data).success(function () {
                $scope.smssuc = 'Sms sent successfully.';
            }).error(function () {
                $scope.smserr = 'Error, while sending sms.';
            });
        };

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