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
            console.log('followers');
            $scope.followers = data;
        }).error(function () {
            alert(404);
        });

        function getData(){
            console.log($scope.order.customerId);
            orderAPI.getCustomerById($scope.order.customerId).success(function(data){
                $scope.customer = data;
            }).error(function () {
                alert(404);
            });
        }
    });