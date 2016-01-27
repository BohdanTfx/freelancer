;
(function() {

	angular.module(
			'FreelancerApp',
			[ 'ngRoute', 'ui.router', 'ngMaterial', 'isteven-multi-select',
					'vds.multirange' ]).config(
			function($stateProvider, $urlRouterProvider, $locationProvider) {
				// ,'vds.multirange'
				$urlRouterProvider.otherwise('/orders');

				// routes
				$stateProvider.state('orders', {
					url : '/orders',
					templateUrl : 'app/components/jobs/jobs.html',
					controller : 'jobsCtrl'
				}).state('personal', {
					url : '/personal',
					templateUrl : 'app/components/personal/personal.html',
					controller : 'personalCtrl'
				});

				$locationProvider.html5Mode(false);

			}).run(function() {
	});

})();
