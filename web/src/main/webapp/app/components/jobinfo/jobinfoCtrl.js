angular.module('FreelancerApp')
    .controller('jobinfoCtrl', function ($scope, orderAPI, $stateParams, $log) {
        var orderId = $stateParams.orderId;
        orderAPI.getOrderById(orderId).success(function (data) {
            $scope.order = data;
            getData();
        }).error(function () {
            alert("Order doesn't exist");
        });

        orderAPI.getFollowers(orderId).success(function (data) {
            $scope.followers = data;
            if (data.length == 0) {
                $scope.noFollowers = true;
            }else{
                for(var i in $scope.followers){
                    $scope.setDevRating($scope.followers[i]);
                }
            }
        }).error(function () {
            alert(404);
        });

        orderAPI.getOrderTechs(orderId).success(function (data) {
            $scope.techs = data;
        }).error(function () {
            alert(404);
        });

        function getData() {
            orderAPI.getCustomerById($scope.order.customerId).success(function (data) {
                $scope.customer = data;
            }).error(function () {
                alert(404);
            });

            orderAPI.getCustomerFeedbacks($scope.order.customerId).success(function (data) {
                $scope.feedbacks = data;
                if(data.length == 0) {
                    $scope.noFeedbacks = true;
                }
                $scope.calcCustRate();
            }).error(function () {
                alert(404);
            });

            orderAPI.getCustomerHistory($scope.order.customerId).success(function (data) {
                $scope.custProjects = data;
                $log.log(data);
                if(data.length == 0) {
                    $scope.emptyHistory = true;
                }
            }).error(function () {
                alert(404);
            });
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
                    console.log("rate in func = " + data);
                    follower.rate = data;
                }).error(function () {
                    follower.rate = 0;
                });
        };

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
    });