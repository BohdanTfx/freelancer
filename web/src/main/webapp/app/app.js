;
(function() {

	angular
			.module(
					'FreelancerApp',
					[ 'ngRoute', 'ui.router', 'ngMaterial' ])
			.config(
					function($stateProvider, $urlRouterProvider,
							$locationProvider) {

						$urlRouterProvider.otherwise('/orders');

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
										'personal',
										{
											url : '/personal',
											templateUrl : 'app/components/personal/personal.html',
											controller : 'personalCtrl'
										});

						$locationProvider.html5Mode(false);

					}).run(function() {
			});

})();
