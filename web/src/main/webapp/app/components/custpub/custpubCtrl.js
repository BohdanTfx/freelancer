angular.module('FreelancerApp')
    .controller('custpubCtrl', function ($scope, custpubAPI, $log, $http, $location, $filter, $stateParams, $rootScope, Notification, $translate) {

        $scope.query = $stateParams.custId;
        $scope.deleteFeedId = $rootScope.id;
        $scope.feedback = '';
        $scope.hiremes = '';
        $scope.mes = '';
        $scope.own = false;
        $scope.custOwn = false;

        if ($rootScope.id == $stateParams.custId) {
            $scope.own = true;
        }

        if ($rootScope.role == 'customer') {
            $scope.custOwn = true;
        }

        if ($rootScope.role == 'customer') {
            $scope.show = true;
            $scope.feedShow = false;
        } else {
            $scope.show = false;
            $scope.feedShow = true;
        }

        $scope.wmDis = false;
        if ($rootScope.id == $stateParams.custId) {
            $scope.wmDis = true;
        }

        $scope.deleteFeed = function (devId, feedId) {
            custpubAPI.deleteFeed(devId, feedId).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('custpub.notif-feed-notif')
                    });
                $scope.feed();
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.notif-smth-wrong-try')
                    });
            });
        };

        $http.post('/user/isAuth').success(function (data) {
            $scope.mesEmail = data.email;
        }).error(function () {
        });

        custpubAPI.getContForCust($scope.query).success(
            function (data, status, headers, config) {
                if (typeof data.skype != 'undefined')
                    $scope.skype = 'Skype: ' + data.skype;
                else
                    $scope.skype = undefined;
                $scope.phone = data.phone;
            }).error(function () {

            });

        custpubAPI.getAvailableCustOrders($scope.query).success(function (data) {
            if ((typeof data == 'undefined' || data == null || data.length == 0) && !$scope.show)
                $scope.show = true;
            else {
                $scope.avords = data;
                $scope.show = false;
            }
        }).error(function () {
            $scope.show = false;
        });

        custpubAPI.getRateForCust($scope.query).success(
            function (data, status, headers, config) {
                if (typeof data != 'undefined')
                    $scope.rateq = data;
                else
                    $scope.rateq = undefined;
            }).error(function () {

            });

        custpubAPI.getOrdPubHist($scope.query).success(
            function (data, status, headers, config) {
                $scope.orders = data;
                if (data.length == 0) {
                    $scope.emptyHist = true;
                    $scope.noneHist = $translate.instant('custpub.there-is-no-hist');
                }

            }).error(function () {
                $scope.emptyHist = true;
                $scope.noneHist = $translate.instant('custpub.there-is-no-hist');
            });

        $scope.feed = function () {
            custpubAPI.getFeedForCust($scope.query).success(
                function (data, status, headers, config) {
                    if (data.length !== 0) {
                        $scope.feeds = data;

                        for (var i = 0; i < $scope.feeds.length; i++) {
                            if (typeof $scope.feeds[i].developer.imgUrl == 'undefined' || $scope.feeds[i].developer.imgUrl == null)
                                $scope.feeds[i].developer.imgUrl = 'images/profile/no-image.png';
                            else
                                $scope.feeds[i].developer.imgUrl += 'md.jpg';
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
            if (typeof $scope.hiremes == 'undefined' || $scope.hiremes == '' || typeof $scope.hireord == 'undefined' || $scope.hireord == '') {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('pubdev.empty-fields')
                    });
                return;
            }
            var data = 'phone=' + $scope.phone + '&order_id=' + $scope.hireord +
                '&dev_id=' + $rootScope.id + "&message=" + $scope.hiremes + '&cust_id=' + $scope.query + '&author=dev';
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/sms', data).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('custpub.you-are-follow-him')
                    });
                $scope.hiremes = '';
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.error')
                    });
                $scope.hiremes = '';
            });
        };

        $scope.rating = function () {
            custpubAPI.getRateForCust($scope.query).success(
                function (data, status, headers, config) {
                    $scope.rateq = data;
                }).error(function () {

                });
        };

        $scope.rating();

        var getCust = function (cust_id) {
            custpubAPI.getCustById(cust_id).success(
                function (data, status, headers, config) {
                    if (data) {

                    } else {
                        $scope.customerNotFound = true;
                    }
                    $scope.id = data.id;
                    if (typeof data.imgUrl == 'undefined' || data.imgUrl == null)
                        $scope.img = 'images/profile/no-image.png';
                    else
                        $scope.img = data.imgUrl + "md.jpg";
                    $scope.email = data.email;
                    $scope.fname = data.fname;
                    $scope.lname = data.lname;
                    $scope.regDate = data.regDate;
                    if (typeof data.overview != 'undefined' && data.overview != null) {
                        $scope.overview = data.overview;
                        $scope.overHead = $translate.instant('custpub.overview');
                    } else {
                        $scope.overview = undefined;
                        $scope.overHead = undefined;
                        $scope.noneOver = $translate.instant('custpub.nothing-to-show');
                    }

                }).error(function () {
                    $scope.freelancerNotFound = true;
                });
        };
        getCust($scope.query);

        $scope.send = function () {
            $scope.dataLoading = true;
            if ($scope.mes == '' || typeof $scope.mes == 'undefined') {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('pubdev.empty-fields')
                    });
                $scope.dataLoading = false;
                return;
            }
            var data = 'message=' + $scope.mes + '&email=' + $scope.email + '&subject=Customer sent you message: ' + $rootScope.name + ' ' + $rootScope.lastName;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/send', data).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('custpub.your-email-was-succ-sent-to') + $rootScope.name
                    });
                $scope.dataLoading = false;
                $scope.mes = '';
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.an-error-occured-while-sending-to') + $rootScope.name + $translate.instant('custpub.try-again')
                    });
                $scope.dataLoading = false;
                $scope.mes = '';
            });
        };

        $scope.comment = function (rate, feedback) {
            $scope.feedback = '';
            if (rate != 0) {
                var data = 'comment=' + feedback + '&id=' + $scope.id + '&rate=' + rate + '&role=dev';
                $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            } else {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.rating-cant-be')
                    });
                $scope.comsuc = undefined;
                return;
            }
            if (typeof feedback != 'undefined' && typeof rate != 'undefined') {
                $http.post('/user/comment', data).success(function () {
                    Notification
                        .success({
                            title: $translate.instant('notification.success'),
                            message: $translate.instant('custpub.feed-was-succ-added')
                        });
                    $scope.comerr = undefined;

                    $scope.feed();
                    $scope.emptyComm = false;
                    $scope.rating();

                    $scope.noneFeed = undefined;


                }).error(function () {
                    Notification
                        .error({
                            title: $translate.instant('notification.error'),
                            message: $translate.instant('custpub.please-inp-mess-and-try')
                        });
                    $scope.comsuc = undefined;
                });
            } else {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('custpub.error-while-send-feed')
                    });
                $scope.comsuc = undefined;
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