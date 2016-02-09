angular.module('FreelancerApp')
    .controller('forgotCtrl',['$scope', '$rootScope', 'forgotAPI',function($scope, $rootScope, forgotAPI){
        $scope.email = '';
        $scope.confirmEmailFlag = false;

        $scope.checkEmail = function (){
            forgotAPI.confirmUserEmail($scope.email).success(function (data){
                $scope.result = data;
                $scope.confirmEmailFlag = true;
            }).error(function (response) {
                Notification.error({
                    title: 'Error!',
                    message: 'Wrong email. Try again!'
                });
            });
        }

    }]);