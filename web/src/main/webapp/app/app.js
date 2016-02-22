;
(function() {
	angular
			.module(
					'FreelancerApp',
					[ 'ngRoute', 'ui.router', 'ngCookies', 'ngMaterial',
							'ngAnimate', 'FreelancerApp.Filters', 'ngAria',
							'ngMessages', 'isteven-multi-select', 'rzModule',
							'ui.bootstrap', 'ui-notification', 'googlechart',
							'pascalprecht.translate', 'tmh.dynamicLocale' ])
			.config(
					function($stateProvider, $urlRouterProvider,
							$locationProvider, NotificationProvider) {
						$urlRouterProvider.otherwise('/home');

						NotificationProvider.setOptions({
							delay : 5000,
							startTop : 75,
							startRight : 10,
							verticalSpacing : 20,
							horizontalSpacing : 20,
							positionX : 'right',
							positionY : 'top'
						});

						// routes
						$stateProvider
								.state(
										'orders',
										{
											url : '/orders',
											templateUrl : 'app/components/jobs/jobs.html',
											controller : 'jobsCtrl'
										})
								.state(
										'developers',
										{
											url : '/developers',
											templateUrl : 'app/components/developers/developers.html',
											controller : 'developersCtrl'
										})
								.state(
										'createOrder',
										{
											url : '/order/create',
											templateUrl : 'app/components/order/order.html',
											controller : 'orderCtrl'
										})
								.state(
										'order',
										{
											url : '/orders/:orderId',
											templateUrl : 'app/components/jobinfo/jobinfo.html',
											controller : 'jobinfoCtrl'
										})
								.state(
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
										})
								.state(
										'myworks',
										{
											url : '/myworks',
											templateUrl : 'app/components/myworks/myworks.html',
											controller : 'myworksCtrl'
										})
								.state(
										'test',
										{
											url : '/tests/:testId',
											templateUrl : 'app/components/test/test.html',
											controller : 'testCtrl'
										})
								.state(
										'home',
										{
											url : '/home',
											templateUrl : 'app/components/home/home.html',
											controller : 'homeCtrl'
										})
								.state(
										'auth',
										{
											url : '/auth',
											templateUrl : 'app/components/authentication/auth.html',
											controller : 'authCtrl'
										})
								.state(
										'pubdev',
										{
											url : '/public/developer/:devName/:devId',
											templateUrl : 'app/components/pubdev/pubdev.html',
											controller : 'pubdevCtrl'
										})
								.state(
										'custpub',
										{
											url : '/public/customer/:custName/:custId',
											templateUrl : 'app/components/custpub/custpub.html',
											controller : 'custpubCtrl'
										})
								.state(
										'createtest',
										{
											url : '/tests/creating',
											templateUrl : 'app/components/createtest/createtest.html',
											controller : 'createtestCtrl'
										})
								.state(
										'technologies',
										{
											url : '/technologies',
											templateUrl : 'app/components/technologies/technologies.html',
											controller : 'technologiesCtrl'
										})
								.state(
										'statistics',
										{
											url : '/admin/statistics',
											templateUrl : 'app/components/statistics/statistics.html',
											controller : 'statisticsCtrl'
										})
								.state(
										'createAdmin',
										{
											url : '/admin/new/admin',
											templateUrl : 'app/components/createadmin/createadmin.html',
											controller : 'createadminCtrl'
										})
								.state(
										'signupadmin',
										{
											url : '/signup/:uuid',
											templateUrl : 'app/components/signup/signup.html',
											controller : 'signupCtrl'
										})
								.state(
										'forgot',
										{
											url : '/forgot',
											templateUrl : 'app/components/forgotpass/forgot.html',
											controller : 'forgotCtrl'
										})
								.state(
										'banservice',
										{
											url : '/admin/banservice',
											templateUrl : 'app/components/ban/ban.html',
											controller : 'banCtrl'
										});

						$locationProvider.html5Mode(false);
					})
			.constant('LOCALES', {
				'locales' : {
					'uk_UA' : 'Українська',
					'en_US' : 'English'
				},
				'preferredLocale' : 'en_US'
			})
			.config(function($translateProvider) {
				$translateProvider.useMissingTranslationHandlerLog();
			})
			.config(function($translateProvider) {
				$translateProvider.useStaticFilesLoader({
					prefix : 'app/locales/locale-',// path to translations
					// files
					suffix : '.json'// suffix, currently- extension of the
				// translations
				});
				$translateProvider.preferredLanguage('en_US');// is applied on
				// first load
				$translateProvider.useLocalStorage();// saves selected
				// language
				// to localStorage
			})
			.config(
					function(tmhDynamicLocaleProvider) {
						tmhDynamicLocaleProvider
								.localeLocationPattern('../bower_components/angular-i18n/angular-locale_{{locale}}.js');
					})
			.run(
					[
							'$rootScope',
							'$location',
							'$cookieStore',
							'$http',
							'AuthenticationService',
							'Notification',
							'$state',
							'$timeout',
							'$interval',
							function($rootScope, $location, $cookieStore,
									$http, AuthenticationService, Notification,
									$state, $timeout, $interval) {
								$rootScope.globals = {};
								$rootScope.logged = false;

								$rootScope.logout = function() {
									AuthenticationService.ClearCredentials();
									$state.go('home');
									hello('google').logout().then(function() {
									}, function(e) {
									});
								};

								var nonAbstractInterval = $interval(
										function() {
											if (!$state.current['abstract']) {
												$interval
														.cancel(nonAbstractInterval);

												AuthenticationService
														.autoAuthenticate()
														.success(
																function(data) {
																	if (data !== false
																			&& data !== null
																			&& (typeof data === 'object')) {
																		AuthenticationService
																				.SetCredentials(data);
																		$rootScope.logged = true;
																	}
																	if (!hasAccess($state.current.name))
																		$state
																				.go('home');

																	registerStateChangeListener();
																})
														.error(
																function() {
																	registerStateChangeListener();

																	Notification
																			.error({
																				title : 'Error!',
																				message : 'An error occurred while authenticating. Please try again.'
																			});
																});
											}
										}, 25);

								getSavedStateData();

								function registerStateChangeListener() {
									$rootScope
											.$on(
													'$stateChangeStart',
													function(event, toState,
															toParams,
															fromState,
															fromParams) {
														if (!hasAccess(toState.name)) {
															if (!fromState['abstract'])
																$state
																		.go($state.current.name);
															else
																$state
																		.go('auth');
															event
																	.preventDefault();
														}
													});
								}

								function hasAccess(state) {
									var nextStatePermission = getPermissions()[state];
									if ($rootScope.globals.currentUser === undefined) {
										if (nextStatePermission === undefined
												|| nextStatePermission["unknown"] == false)
											return false;

										return true;
									}
									var role = $rootScope.globals.currentUser.role;
									if (nextStatePermission === undefined
											|| nextStatePermission[role] == false)
										return false;

									return true;
								}
							} ]);

})();
