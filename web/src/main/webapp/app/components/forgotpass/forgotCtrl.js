angular.module('FreelancerApp')
    .controller('forgotCtrl',['$scope','$http', '$rootScope', 'forgotAPI', '$log', '$mdDialog','Notification', '$location', function($scope, $http, $rootScope, forgotAPI, $log, $mdDialog, Notification, $location){
        $scope.email = '';
        $scope.newPassword = '';
        $scope.confirmPassword = '';
        $scope.confirmEmailFlag = false;
        $scope.confirmCode = '';

        $scope.checkEmail = function (){
            forgotAPI.confirmUserEmail($scope.emailTemp=$scope.email).success(function (data){
                console.log($scope.emailTemp);
                $scope.result = data;
                $scope.showConfirmDialog();
            }).error(function (response) {
                Notification.error({
                    title: 'Error!',
                    message: 'Wrong email. Try again!'
                });
            });
        };

        $scope.checkConfirmPhone = function(){
            forgotAPI.confirmPhoneCode($scope.emailTemp, $scope.confirmCode).success(function (data){
                console.log($scope.emailTemp);
                $scope.result = data;
                $scope.confirmEmailFlag = true;
                $scope.cancel();
            }).error(function (response) {
                $scope.cancel();
                $scope.confirmEmailFlag = false;
                Notification.error({
                    title: 'Error!',
                    message: 'Wrong confirm code. Try again!'
                });
            });
        };

        $scope.changePassword = function () {
            forgotAPI.changePassword($scope.emailTemp, $scope.confirmPassword).success(function (data){
                console.log($scope.emailTemp);
                $scope.result = data;
                $scope.confirmEmailFlag = false;
                $location.path('/auth');
                Notification.success({
                    title: 'Changes saved!',
                    message: 'The password changed.'
                });
                $scope.cancel();
            }).error(function (response) {
                $scope.confirmEmailFlag = false;
                Notification.error({
                    title: 'Error!',
                    message: 'Some problem. Try again!'
                });
            });
        };

        $scope.showConfirmDialog = function ($event){
            $mdDialog.show({
                parent: angular.element(document.body),
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/forgotpass/confirmPhoneCode.html',
                controller: 'forgotCtrl',
                clickOutsideToClose: true,
                onComplete: afterShowAnimation,
                locals: {confirmPasswordFlag: $scope.confirmPasswordFlag}
            });

            function afterShowAnimation(scope, element, options) {

            }
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.resetPassword = function(){
            $scope.newPassword = '';
            $scope.confirmPassword = '';
        };

    }]);