angular.module('FreelancerApp')
    .controller('custpubCtrl', function ($scope, custpubAPI, $log, $http, $location, $filter, $stateParams, $rootScope, Notification) {
        console.log($stateParams.custName, $stateParams.custId + ' state');

        $scope.query = $stateParams.custId;

        if ($rootScope.role == 'developer') {
            $scope.show = true;
        } else {
            $scope.show = false;
        }

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
                $scope.orders = data;
            }).error(function () {

            });

        $scope.feed = function () {
            custpubAPI.getFeedForCust($scope.query).success(
                function (data, status, headers, config) {
                    console.log(data);
                    if (data.length !== 0) {
                        $scope.feeds = data;

                        for (var i = 0; i < $scope.feeds.length; i++) {
                            if (typeof $scope.feeds[i].developer.imgUrl == 'undefined' || $scope.feeds[i].developer.imgUrl == null)
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
                Notification
                    .success({
                        title : 'Success',
                        message : 'Now you are follow ' + $rootScope.name
                    });
            }).error(function () {
                Notification
                    .error({
                        title : 'Error',
                        message : 'Error, while sending sms. Please try again. Possibly, your phone number not valid/'
                    });
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
                Notification
                    .success({
                        title : 'Success',
                        message : 'Your email was successfully sent to '+ $rootScope.name
                    });
                $scope.dataLoading = false;
            }).error(function () {
                Notification
                    .error({
                        title : 'Error',
                        message : 'An error occurred while sending email to ' +$rootScope.name + '. Please try again.'
                    });
                $scope.dataLoading = false;
            });
        };

        $scope.comment = function (rate, feedback) {
            if (rate != 0) {
                var data = 'comment=' + feedback + '&id=' + $scope.id + '&rate=' + rate + '&role=dev';
                $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';
            } else {
                Notification
                    .error({
                        title : 'Error',
                        message : 'Rating cant be 0'
                    });
                $scope.comsuc = undefined;
                return;
            }
            if (typeof feedback != 'undefined' && typeof rate != 'undefined') {
                $http.post('/user/comment', data).success(function () {
                    Notification
                        .success({
                            title : 'Success',
                            message : 'Feedback was successfully added.'
                        });
                    $scope.comerr = undefined;

                    $scope.feed();
                    $scope.rating();

                    $scope.noneFeed = undefined;


                }).error(function () {
                    Notification
                        .error({
                            title : 'Error',
                            message : 'Please, input message and try again'
                        });
                    $scope.comsuc = undefined;
                });
            } else {
                Notification
                    .error({
                        title : 'Error',
                        message : 'Error, while sending feedback.'
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