angular.module('FreelancerApp')
    .controller('custpubCtrl', function ($scope, custpubAPI, $log, $http, $location, $filter, $stateParams, $rootScope) {
        console.log($stateParams.custName, $stateParams.custId + ' state');

        $scope.query = $stateParams.custId;

        $http.post('/user/isAuth').success(function (data) {
            console.log(data);
            $scope.mesEmail = data.email;
        }).error(function () {
        });

        custpubAPI.getContForCust($scope.query).success(
            function (data, status, headers, config) {
                console.log(data);
                if (typeof data.skype != 'undefined')
                    $scope.skype = 'Skype: ' + data.skype;
                else
                    $scope.skype = undefined;
            }).error(function () {

            });

        custpubAPI.getRateForCust($scope.query).success(
            function (data, status, headers, config) {
                console.log(data);
                if (typeof data != 'undefined')
                    $scope.rateq = data;
                else
                    $scope.rateq = undefined;
            }).error(function () {

            });

        custpubAPI.getOrdPubHist($scope.query).success(
            function (data, status, headers, config) {
                console.log(data);
            }).error(function () {

            });

        $scope.feed = function () {
            custpubAPI.getFeedForCust($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    if (data.length !== 0) {
                        $scope.feeds = data;

                        for (var i = 0; i < $scope.feeds.length; i++) {
                            if (typeof $scope.feeds[i].developer.imgUrl == 'undefined')
                                $scope.feeds[i].developer.imgUrl = 'images/profile/no-profile-img-head.gif';
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
                $scope.smssuc = 'Sms sent successfully.';
            }).error(function () {
                $scope.smserr = 'Error, while sending sms.';
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
                    console.log(data);
                    $scope.id = data.id;
                    if (typeof data.imgUrl == 'undefined' || data.imgUrl == null)
                        $scope.img = 'images/profile/no-profile-img.jpg';
                    else
                        $scope.img = data.imgUrl;
                    $scope.email = data.email;
                    $scope.fname = data.fname;
                    $scope.lname = data.lname;
                    $scope.regDate = data.regDate;
                    if (typeof data.overview != 'undefined' && data.overview != null) {
                        $scope.overview = data.overview;
                        $scope.overHead = 'Overview';
                    } else {
                        $scope.overview = undefined;
                        $scope.overHead = undefined;
                        $scope.noneOver = 'Nothing to show';
                    }

                }).error(function () {
                    $scope.freelancerNotFound = true;
                });
        };

        getCust($scope.query);

        $scope.send = function () {
            $scope.dataLoading = true;
            var data = 'message=' + $scope.mes + '&email=' + $scope.email + '&changeEmail=' + $scope.mesEmail;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/send', data).success(function () {
                $scope.messuc = 'Message sent successfully.';
                $scope.dataLoading = false;
            }).error(function () {
                $scope.messerr = 'Error, while sending message.';
                $scope.dataLoading = false;
            });
        };

        $scope.comment = function (rate, feedback) {
            if (rate != 0) {
                var data = 'comment=' + feedback + '&id=' + $scope.id + '&rate=' + rate;
                $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            } else {
                $scope.comerr = 'Error, bad value.';
                $scope.comsuc = undefined;
                return;
            }
            if (typeof feedback != 'undefined' && typeof rate != 'undefined') {
                $http.post('/user/comment', data).success(function () {
                    $scope.comsuc = 'Comment sent successfully.';
                    $scope.comerr = undefined;

                    $scope.feed();
                    $scope.rating();

                    $scope.noneFeed = undefined;

                }).error(function () {
                    $scope.comerr = 'Error, bad value.';
                    $scope.comsuc = undefined;
                });
            } else {
                $scope.comerr = 'Error, bad value.';
                $scope.comsuc = undefined;
            }
        };
    });