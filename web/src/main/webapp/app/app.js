;
(function() {
	angular
			.module(
					'FreelancerApp',
					[ 'ngRoute', 'ui.router', 'ngCookies', 'ngMaterial',
							'ngAnimate', 'ngAria', 'ngMessages',
							'isteven-multi-select', 'rzModule' ])
			.config(
					function($stateProvider, $urlRouterProvider,
							$locationProvider) {
                        $urlRouterProvider.otherwise('/home');

            // routes
            $stateProvider
                .state(
                'orders',
                {
                    url : '/orders',
                    templateUrl : 'app/components/jobs/jobs.html',
                    controller : 'jobsCtrl'
                }).state(
                'signup',
                {
                    url : '/signup',
                    templateUrl : 'app/components/signup/signup.html',
                    controller : 'signupCtrl'
                })
                .state(
                'personal',
                {
                    url : '/personal',
                    templateUrl : 'app/components/personal/personal.html',
                    controller : 'personalCtrl'
                })
                .state(
                'tests',
                {
                    url : '/tests',
                    templateUrl : 'app/components/tests/tests.html',
                    controller : 'testsCtrl'
                }).state('test', {
                    url: '/tests/:testId',
                    templateUrl: 'app/components/test/test.html',
                    controller: 'testCtrl'
                }).state('home', {
                    url: '/home',
                    templateUrl: 'app/components/home/home.html',
                })
                .state(
                'auth',
                {
                    url : '/auth',
                    templateUrl : 'app/components/authentication/auth.html',
                    controller : 'authCtrl'
                }).state(
                'pubdev',
                {
                    //url: '/pubdev/:devName/:devId',
                    url: '/pubdev',
                    templateUrl: 'app/components/pubdev/pubdev.html',
                    controller: 'pubdevCtrl'
                });

						$locationProvider.html5Mode(false);
					})
			.run(
					[
							'$rootScope',
							'$location',
							'$cookieStore',
							'$http',
                        'AuthenticationService',
                        function ($rootScope, $location, $cookieStore, $http, AuthenticationService) {
                            $rootScope.logout = function () {
                                // reset login status
                                AuthenticationService.ClearCredentials();

                            };
								$rootScope
										.$on(
												'$locationChangeStart',
                                    function (event, next, current, $scope) {
													// redirect to login page if
													// not logged in
													/*
													 * if (typeof
													 * $rootScope.globals.currentUser ==
													 * 'undefined') if
													 * ($location.path() !==
													 * '/auth') {
													 * $location.path('/auth'); }
													 */
                                        $rootScope.globals = {};
                                        $rootScope.logged = false;
                                        $http.post('/user/isAuth').success(function (data) {
                                            console.log(data);
                                            $rootScope.name = data.fname;
                                            $rootScope.lastName = data.lname;
                                            $rootScope.role = data.role;
                                            $rootScope.logged = true;
                                        }).error(function () {
                                        });
                                        console.log('rootScope ' + $rootScope.globals);

                    });
            } ]);

})();
