angular.module('FreelancerApp')
    .controller('jobinfoCtrl', function ($scope, orderAPI, $stateParams, $rootScope, $log, $http, Notification) {

        $scope.user = {};
        $scope.user.id = $rootScope.id;
        $scope.user.role = $rootScope.role;

        $scope.user.subscribed = false;

        if ($scope.user.role == 'developer') {
            $scope.isDev = true;
        } else {
            $scope.show = false;
        }

        var orderId = $stateParams.orderId;
        orderAPI.getOrderById(orderId).success(function (data) {
            $scope.order = data;
            if ($scope.order == undefined) {
                $scope.noOrder = true;
            } else {
                getData();
            }
        }).error(function () {
            alert("Order doesn't exist");
        });


        function getData() {
            orderAPI.getCustomerById($scope.order.customerId).success(function (data) {
                $scope.customer = data;
            }).error(function (e) {
                alert(404);
            });

            orderAPI.getCustomerContactById($scope.order.customerId).success(function (data) {
                $scope.customer.contact = data;
            }).error(function (e) {
                alert(404);
            });

            orderAPI.getCustomerFeedbacks($scope.order.customerId).success(function (data) {
                $scope.feedbacks = data;
                if (data.length == 0) {
                    $scope.noFeedbacks = true;
                }
                $scope.calcCustRate();
            }).error(function () {
                alert(404);
            });

            orderAPI.getCustomerHistory($scope.order.customerId).success(function (data) {
                $scope.custProjects = data;
                if (data.length == 0) {
                    $scope.emptyHistory = true;
                }
            }).error(function () {
                alert(404);
            });

            orderAPI.getFollowers(orderId).success(function (data) {
                $scope.followers = data;
                if (data.length == 0) {
                    $scope.noFollowers = true;
                } else {
                    for (var i in $scope.followers) {
                        $scope.setDevRating($scope.followers[i]);
                    }
                    console.log($scope.user.subscribed);
                    $scope.isSubscriber();
                    console.log($scope.user.subscribed);
                }
            }).error(function () {
                alert(404);
            });

            orderAPI.getOrderTechs(orderId).success(function (data) {
                $scope.techs = data;
            }).error(function () {
                alert(404);
            });

            $scope.calcCustRate = function () {
                var rate = 0;
                var fbs = $scope.feedbacks;
                for (var i = 0; i < fbs.length; i++) {
                    rate += fbs[i].rate;
                }
                rate /= fbs.length;
                rate = parseInt(rate, 10);
                $scope.custRate = rate;
            };

        }

        $scope.getNumber = function (count) {

            var ratings = [];

            for (var i = 0; i < count; i++) {
                ratings.push(i)
            }

            return ratings;
        };

        $scope.setDevRating = function (follower) {
            orderAPI.getRateById(follower.devId).success(
                function (data, status, headers, config) {
                    follower.rate = data;
                }).error(function () {
                    follower.rate = 0;
                });
        };

        $scope.isSubscriber = function(){
            for(var i in $scope.followers){
                if($scope.followers[i].devId == $scope.user.id){
                    $scope.user.subscribed = true;
                    break;
                }
            }
        }

        $scope.subscribe = function (message) {
            if(message == undefined) {
                message="";
            }
            var data = 'message=' + message + '&orderId=' + $stateParams.orderId;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/order/subscribe', data).success(function (data) {
                Notification
                    .success({
                        title: 'Success!',
                        message: 'You successfully subscribed on this project'
                    });
                $scope.followers.push(data);
                $scope.user.subscribed = true;
                $scope.noFollowers = false;

            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Something went bad. Please try again.'
                    });
            });

        };


        $scope.unsubscribe = function () {
            var followerId;
            for(var i in $scope.followers){
                if($scope.followers[i].devId == $scope.user.id){
                    followerId = $scope.followers[i].id;
                }
            }
            var data = 'followerId=' + followerId;

            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/order/unsubscribe', data).success(function () {
                Notification
                    .success({
                        title: 'Success!',
                        message: 'You successfully unsubscribed from this project'
                    });
                $scope.user.subscribed = false;
                for(var i in $scope.followers){
                    if($scope.followers[i].devId == $scope.user.id){
                        $scope.followers.splice(i, 1);
                        if($scope.followers.length == 0){
                            $scope.noFollowers = true;
                        }
                    }
                }

            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Something went bad. Please try again.'
                    });
            });

        };


    });