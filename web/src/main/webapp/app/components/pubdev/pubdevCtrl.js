angular.module('FreelancerApp')
    .controller('pubdevCtrl', function ($scope, pubdevAPI, $log, $http, $location, $filter, $stateParams, $rootScope, Notification) {
        console.log($stateParams.devName, $stateParams.devId + ' state');

        $scope.userrole = $rootScope.role;

        if ($scope.userrole == 'developer') {
            $scope.show = false;
        } else {
            $scope.show = true;
        }

        $scope.query = $stateParams.devId;
        $http.post('/user/isAuth').success(function (data) {
            console.log(data);
            $scope.mesEmail = data.email;
        }).error(function () {
        });

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
                if (data) {

                } else {
                    $scope.freelancerNotFound = true;
                }
                console.log(data + ' dev');
                $scope.id = data.id;
                if (typeof data.imgUrl == 'undefined' || data.imgUrl == null)
                    $scope.img = 'images/profile/no-profile-img.jpg';
                else
                    $scope.img = data.imgUrl;
                $scope.email = data.email;
                $scope.fname = data.fname;
                $scope.lname = data.lname;
                if (typeof data.hourly != 'undefined' && data.hourly != null) {
                    $scope.hourly = '$ ' + data.hourly + '/hr';
                }
                else {
                    $scope.hourly = undefined;
                }
                $scope.regDate = data.regDate;
                if (typeof data.overview != 'undefined' && data.overview != null) {
                    $scope.overview = data.overview;
                    $scope.overHead = 'Overview';
                } else {
                    $scope.overview = undefined;
                    $scope.overHead = undefined;
                    $scope.noneOver = 'Nothing to show';
                }
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

        pubdevAPI.getTestByDevId($scope.query).success(
            function (data, status, headers, config) {
                if (data.length != 0) {
                    $scope.qas = data;
                    $scope.outOfDate = 'Out of date';
                    $scope.passed = 'Passed';
                    $scope.notPassed = 'Not passed'
                } else {
                    $scope.emptyTest = true;
                }
            }).error(function () {
            });

        pubdevAPI.getContById($scope.query).success(
            function (data, status, headers, config) {
                console.log(data);
                if (typeof data.skype != 'undefined')
                    $scope.skype = 'Skype: ' + data.skype;
                else
                    $scope.skype = undefined;
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

        $scope.rating = function () {
            pubdevAPI.getRateById($scope.query).success(
                function (data, status, headers, config) {
                    $scope.rateq = data;
                }).error(function () {

                });
        };
        $scope.rating();

        $scope.feed = function () {
            pubdevAPI.getFeed($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    if (data.length !== 0) {
                        $scope.feeds = data;

                        for (var i = 0; i < $scope.feeds.length; i++) {
                            if (typeof $scope.feeds[i].customer.imgUrl == 'undefined' || $scope.feeds[i].customer.imgUrl == null)
                                $scope.feeds[i].customer.imgUrl = 'images/profile/no-profile-img-head.gif';
                        }
                    }
                    else {
                        $scope.emptyComm = true;
                        $scope.noneFeed = 'There is no feedback';
                    }
                }).error(function () {
                    $scope.emptyComm = true;
                    $scope.noneFeed = 'There is no feedback';
                });
        };

        $scope.feed();

        $scope.sendSms = function () {
            $scope.sms = 'sms';
            var data = 'phone=' + $scope.phone + '&sms=' + $scope.sms;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/sms', data).success(function () {
                Notification
                    .success({
                        title: 'Success!',
                        message: 'Sms sent successfully. Please try again.'
                    });
            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Error while sending sms. Please try again.'
                    });
            });
        };

        $scope.send = function () {
            $scope.dataLoading = true;
            var data = 'message=' + $scope.mes + '&email=' + $scope.email + '&changeEmail=' + $scope.mesEmail;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/send', data).success(function () {
                Notification
                    .success({
                        title: 'Success!',
                        message: 'The message was successfully sent. Please try again.'
                    });
                $scope.dataLoading = false;
            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Error while sending message. Please try again.'
                    });
                $scope.dataLoading = false;
            });
        };

        $scope.comment = function (rate, feedback) {
            if (rate != 0) {
                var data = 'comment=' + feedback + '&id=' + $scope.id + '&rate=' + rate + '&role=customer';
                $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            } else {
                Notification
                    .error({
                        title: 'Error',
                        message: 'Rate cant be 0. Please try again.'
                    });
                return;
            }
            if (typeof feedback != 'undefined' && typeof rate != 'undefined') {
                $http.post('/user/comment', data).success(function () {
                    $scope.feed();
                    $scope.rating();

                    $scope.noneFeed = undefined;
                    Notification
                        .success({
                            title: 'Success!',
                            message: 'Feedback was successfully sent . Please try again.'
                        });

                }).error(function () {
                    Notification
                        .error({
                            title: 'Error!',
                            message: 'Something went bad. Please try again.'
                        });
                });
            } else {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Fields or field are/is empty. Please try again.'
                    });
            }
        };

        $scope.getNumber = function (count) {

            var ratings = [];

            for (var i = 0; i < count; i++) {
                ratings.push(i)
            }

            return ratings;
        };

        $scope.rate = 1;
        $scope.max = 5;
        $scope.isReadonly = false;

        $scope.ratingStates = [
            {stateOn: 'glyphicon-ok-sign', stateOff: 'glyphicon-ok-circle'},
            {stateOn: 'glyphicon-star', stateOff: 'glyphicon-star-empty'},
            {stateOn: 'glyphicon-heart', stateOff: 'glyphicon-ban-circle'},
            {stateOn: 'glyphicon-heart'},
            {stateOff: 'glyphicon-off'}
        ];
    });