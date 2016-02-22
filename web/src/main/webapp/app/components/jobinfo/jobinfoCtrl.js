angular.module('FreelancerApp')
    .controller('jobinfoCtrl', function ($scope, orderAPI, $stateParams, $rootScope, $log, $http, Notification, $translate, $state) {

        $scope.user = {};
        $scope.user.id = $rootScope.globals.currentUser.id;
        $scope.user.role = $rootScope.globals.currentUser.role;

        $scope.user.subscribed = false;

        if ($scope.user.role == 'customer') {
            $scope.isCust = true;
        } else if ($scope.user.role == 'developer') {
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
            Notification
                .error({
                    title: $translate.instant('notification.error'),
                    message: $translate.instant('jobinfo.order-doesnt-exist')
                });
        });

        $scope.complain = function (orderId) {
            orderAPI.toComplain($stateParams.orderId).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('jobinfo.complaining-success')
                    });
                $scope.disCompBut = true;
            }).error(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('jobinfo.complaining-error')
                    })
            });
        };

        $scope.compButDis = orderAPI.isCompAlrEx(orderId).success(function () {
            $scope.disCompBut = true;
        }).error(function () {
            $scope.disCompBut = false;
        });

        function getData() {
            orderAPI.getCustomerById($scope.order.customerId).success(function (data) {
                $scope.customer = data;
                if ($scope.customer.imgUrl == 'undefined' || $scope.customer.imgUrl == null) {
                    $scope.customer.imgUrl = 'images/profile/no-image.png';
                }
                $scope.getCustomerContact();

                if ($scope.isCust && $scope.user.id == $scope.customer.id) {
                    $scope.isJobOwner = true;
                }
            }).error(function (e) {
                Notification
                    .success({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('jobinfo.customer-doesnt-exist')
                    })
            });

            $scope.getCustomerContact = function () {
                orderAPI.getCustomerContactById($scope.order.customerId).success(function (data) {
                    $scope.customer.contact = data;
                }).error(function () {
                })
            };

            orderAPI.getCustomerFeedbacks($scope.order.customerId).success(function (data) {
                $scope.feedbacks = data;
                if (data.length == 0) {
                    $scope.noFeedbacks = true;
                } else {
                    for (var i = 0; i < $scope.feedbacks.length; i++) {
                        if (typeof $scope.feedbacks[i].developer.imgUrl == 'undefined' || $scope.feedbacks[i].developer.imgUrl == null)
                            $scope.feedbacks[i].developer.imgUrl = 'images/profile/no-image.png';
                    }
                }
                $scope.calcCustRate();
            }).error(function () {
            });

            orderAPI.getCustomerHistory($scope.order.customerId).success(function (data) {
                $scope.custProjects = data;
                if (data.length == 0) {
                    $scope.emptyHistory = true;
                }
            }).error(function () {
            });

            orderAPI.getFollowers(orderId).success(function (data) {
                $scope.followers = data;
                if (data.length == 0) {
                    $scope.noFollowers = true;
                } else {
                    for (var i in $scope.followers) {
                        $scope.setDevRating($scope.followers[i]);
                        $scope.setIsWorkerInFollower(i);
                    }
                    for (var i = 0; i < $scope.followers.length; i++) {
                        if (typeof $scope.followers[i].developer.imgUrl == 'undefined' || $scope.followers[i].developer.imgUrl == null)
                            $scope.followers[i].developer.imgUrl = 'images/profile/no-image.png';
                        else $scope.followers[i].developer.imgUrl += 'md.jpg';
                    }
                    $scope.isSubscriber();
                }
            }).error(function () {
            });

            orderAPI.getOrderTechs(orderId).success(function (data) {
                $scope.techs = data;
            }).error(function () {
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

        $scope.setIsWorkerInFollower = function (i) {
            orderAPI.isWorker(JSON.stringify($scope.followers[i])).success(function (data) {
                $scope.followers[i].worker = data;
            });
        };

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

        $scope.isSubscriber = function () {
            for (var i in $scope.followers) {
                if ($scope.followers[i].devId == $scope.user.id) {
                    $scope.user.subscribed = true;
                    break;
                }
            }
        };

        $scope.subscribe = function (message) {
            if (message == undefined) {
                message = "";
            }
            var data = 'message=' + message + '&orderId=' + $stateParams.orderId;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/order/subscribe', data).success(function (data) {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('jobinfo.subscribed-success')
                    });
                if (data.developer.imgUrl == 'undefined' || data.developer.imgUrl == null) {
                    data.developer.imgUrl = 'images/profile/no-image.png';
                }
                $scope.followers.push(data);
                $scope.user.subscribed = true;
                $scope.noFollowers = false;
                $scope.message = "";

            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });

        };


        $scope.unsubscribe = function () {
            var followerId;
            for (var i in $scope.followers) {
                if ($scope.followers[i].devId == $scope.user.id) {
                    followerId = $scope.followers[i].id;
                }
            }
            var data = 'followerId=' + followerId;

            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            $http.post('/user/order/unsubscribe', data).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('jobinfo.unsubscribed-success')
                    });
                $scope.user.subscribed = false;
                for (var i in $scope.followers) {
                    if ($scope.followers[i].devId == $scope.user.id) {
                        $scope.followers.splice(i, 1);
                        if ($scope.followers.length == 0) {
                            $scope.noFollowers = true;
                        }
                    }
                }
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });

        };

        $scope.acceptFollower = function (devId) {
            var today = new Date();
            var acceptDate = new Date(new Date(today).setMonth(today.getDay()+5));
            acceptDate = new Date(acceptDate).getTime();

            var customerJSON = JSON.stringify($scope.customer);
            orderAPI.acceptFollower(devId, $scope.order.id, $scope.order.title, customerJSON, acceptDate).then(function(){
                for (var i = 0; i < $scope.followers.length; i++) {
                    if ($scope.followers[i].devId == devId) {
                        $scope.followers[i].worker = true;
                        break;
                    }
                }
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('jobinfo.follower-accept-success')
                    });
            },function() {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });
        };

        $scope.banOrder = function () {
            orderAPI.banOrder($scope.order.id).success(function () {
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('banservice.ban-success')
                    });
                $state.go('banservice');
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });
        };

    });