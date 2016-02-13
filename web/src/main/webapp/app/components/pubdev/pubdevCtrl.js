angular.module('FreelancerApp')
    .controller('pubdevCtrl', function ($scope, pubdevAPI, $log, $http, $location, $filter, $stateParams, $rootScope, Notification, $translate) {

        $scope.userrole = $rootScope.role;
        $scope.hireord = '';
        $scope.deleteFeedId = $rootScope.id;
        $scope.feedback = '';
        $scope.mes = '';
        $scope.hiremes = '';

        $scope.wmDis = false;
        if ($rootScope.id == $stateParams.devId) {
            $scope.wmDis = true;
        }

        if ($scope.userrole == 'developer') {
            $scope.show = false;
            $scope.hire = false;
        } else {
            $scope.hire = true;
            $scope.show = true;
        }

        $scope.deleteFeed = function (custId, feedId) {
            pubdevAPI.deleteFeed(custId, feedId).success(function () {
                $scope.feed();
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('custpub.notif-feed-notif')
                    });
            }).error(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.notif-smth-wrong-try')
                    });
            });
        };

        $scope.query = $stateParams.devId;
        $http.post('/user/isAuth').success(function (data) {
            $scope.mesEmail = data.email;
        }).error(function () {
        });

        var getCust = function (cust_id) {
            pubdevAPI.getCustById(cust_id).success(
                function (data, status, headers, config) {
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
                $scope.id = data.id;
                if (typeof data.imgUrl == 'undefined' || data.imgUrl == null)
                    $scope.img = 'images/profile/no-image.png';
                else {
                    $scope.img = data.imgUrl + "md.jpg";
                }
                $scope.email = data.email;
                $scope.fname = data.fname;
                $scope.lname = data.lname;
                if (typeof data.hourly != 'undefined' && data.hourly != null) {
                    $scope.hourly = '$ ' + data.hourly + '/ ' + $translate.instant('pubdev.hr');
                }
                else {
                    $scope.hourly = undefined;
                }
                $scope.regDate = data.regDate;
                if (typeof data.overview != 'undefined' && data.overview != null) {
                    $scope.overview = data.overview;
                    $scope.overHead = $translate.instant('custpub.overview');
                } else {
                    $scope.overview = undefined;
                    $scope.overHead = undefined;
                    $scope.noneOver = $translate.instant('custpub.nothing-to-show');
                }
                $scope.position = data.position;

            }).error(function () {
                $scope.freelancerNotFound = true;
            });

        pubdevAPI.getTechById($scope.query).success(
            function (data, status, headers, config) {
                $scope.techs = data;
            }).error(function () {

            });

        pubdevAPI.getTestByDevId($scope.query).success(
            function (data, status, headers, config) {
                if (data.length != 0) {
                    $scope.qas = data;
                    $scope.outOfDate = $translate.instant('pubdev.out-of-date');
                    $scope.passed = $translate.instant('pubdev.passed');
                    $scope.notPassed = $translate.instant('pubdev.not-passed');
                } else {
                    $scope.emptyTest = true;
                }
            }).error(function () {
            });

        pubdevAPI.getContById($scope.query).success(
            function (data, status, headers, config) {
                if (typeof data.skype != 'undefined') {
                    $scope.skype = 'Skype: ' + data.skype;
                    $scope.phone = data.phone;
                }

                else
                    $scope.skype = undefined;
            }).error(function () {

            });

        pubdevAPI.getPortById($scope.query).success(
            function (data, status, headers, config) {
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
                    if (data.length !== 0) {
                        $scope.feeds = data;

                        for (var i = 0; i < $scope.feeds.length; i++) {
                            if (typeof $scope.feeds[i].customer.imgUrl == 'undefined' || $scope.feeds[i].customer.imgUrl == null)
                                $scope.feeds[i].customer.imgUrl = 'images/profile/no-image.png';
                            else
                                $scope.feeds[i].customer.imgUrl += 'md.jpg';
                        }
                    }
                    else {
                        $scope.emptyComm = true;
                        $scope.noneFeed = $translate.instant('custpub.there-is-no-feed');
                    }
                }).error(function () {
                    $scope.emptyComm = true;
                    $scope.noneFeed = $translate.instant('custpub.there-is-no-feed');
                });
        };

        $scope.feed();

        $scope.sendSms = function () {
            var data = 'phone=' + $scope.phone + '&order_id=' + $scope.hireord +
                '&dev_id=' + $scope.query + "&message=" + $scope.hiremes + '&cust_id=' + $rootScope.id + '&author=customer';
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/sms', data).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('pubdev.freel-will-see-your-invit')
                    });
                $scope.hiremes = '';
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('pubdev.error-while-send-invit-try-again')
                    });
            });
            $scope.hiremes = '';
        };

        pubdevAPI.getAvailableCustOrders($scope.query).success(function (data) {
            if (typeof data == 'undefined' || data == null || data.length == 0)
                $scope.hire = true;
            else {
                $scope.avords = data;
                $scope.hire = false;
            }
        }).error(function () {
            $scope.hire = false;
        });

        $scope.send = function () {
            $scope.dataLoading = true;
            var data = 'message=' + $scope.mes + '&email=' + $scope.email + '&subject=Customer sent you message: ' + $rootScope.name + ' ' + $rootScope.lastName;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            $scope.mes = '';
            $http.post('/user/send', data).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('pubdev.the-message-was-sent-succ')
                    });
                $scope.dataLoading = false;
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('pubdev.error-while-send-message-try')
                    });
                $scope.dataLoading = false;
            });
            $scope.mes = '';
        };

        $scope.comment = function (rate, feedback) {
            $scope.feedback = '';
            if (rate != 0) {
                var data = 'comment=' + feedback + '&id=' + $scope.id + '&rate=' + rate + '&role=customer';
                $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            } else {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.rating-cant-be')
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
                            title: $translate.instant('notification.success'),
                            message: $translate.instant('custpub.feed-was-succ-added')
                        });

                }).error(function () {
                    Notification
                        .error({
                            title: $translate.instant('notification.error'),
                            message: $translate.instant('pubdev.smth-went-bad-try')
                        });
                });
            } else {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('pubdev.empty-fields')
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