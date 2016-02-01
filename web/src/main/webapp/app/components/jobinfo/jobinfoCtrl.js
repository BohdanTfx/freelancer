angular.module('FreelancerApp')
    .controller('jobinfoCtrl', function ($scope, orderAPI, $stateParams, $log) {
        var orderId = $stateParams.orderId;
        orderAPI.getOrderById(orderId).success(function(data){
            $scope.order = data;
            $log.log(data + ' order');

        }).error(function () {
            alert(404);
        });

        if($scope.order){

        }else{
            orderAPI.getFollowers(orderId).success(function(data){
                console.log('followers');
                $scope.followers = data;
                $log.log(data +  ' followers');
            }).error(function () {
                alert(404);
            });

            orderAPI.getCustomerById().success(function(data){
                $scope.followers = data;
            }).error(function () {
                alert(404);
            });
        }
    });