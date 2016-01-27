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
                $scope.dataLoading = true;
                AuthenticationService.Login($scope.username, $scope.password, $scope.remember).success(function (response) {
                    AuthenticationService.SetCredentials($scope.username, $scope.password, response.role);
                    $location.path('/');

                }).error(function (response) {
                    $scope.dataLoading = false;
                    $scope.error = 'Invalid credentials';
                })
            };

            $scope.logout = function () {
                // reset login status
                AuthenticationService.ClearCredentials();

            }
        }]);
