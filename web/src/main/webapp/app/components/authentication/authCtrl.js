angular.module('FreelancerApp')
    .controller('authCtrl',
    ['$scope', '$rootScope', '$location', '$cookieStore', 'AuthenticationService',
        function ($scope, $rootScope, $location, $cookieStore, AuthenticationService) {

            if (typeof $cookieStore.get('freelancerRememberMeCookieAng') !== 'undefined') {
                $location.path('/');
                return;
            }

            // reset login status
            AuthenticationService.ClearCredentials();

            $scope.login = function () {
                AuthenticationService.Login($scope.username, $scope.password, $scope.remember).success(function (response) {
                    AuthenticationService.SetCredentials(response.fname, response.lname, response.role);
                    $location.path('/');

                }).error(function (response) {
                    $scope.error = 'Invalid credentials';
                })
            };
        }]);