angular.module('FreelancerApp')
    .controller('jobinfoCtrl', function ($scope, orderAPI, $stateParams, $log) {
        var orderId = $stateParams.orderId;
        orderAPI.getOrderById(orderId).success(function(data){
            $scope.order = data;
            getData();
        }).error(function () {
            alert("Order doesn't exist");
        });

        orderAPI.getFollowers(orderId).success(function(data){
            console.log(data);
            $scope.followers = data;
        }).error(function () {
            alert(404);
        });

        orderAPI.getOrderTechs(orderId).success(function(data){
            console.log(data);
            $scope.techs = data;
        }).error(function () {
            alert(404);
        });

        function getData(){
            orderAPI.getCustomerById($scope.order.customerId).success(function(data){
                $scope.customer = data;
            }).error(function () {
                alert(404);
            });

            orderAPI.getCustomerFeedbacks($scope.order.customerId).success(function(data){
                $scope.feedbacks = data;
            }).error(function () {
                alert(404);
            });

        }
    });