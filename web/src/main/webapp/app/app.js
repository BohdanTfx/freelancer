
;(function() {

  angular
    .module('FreelancerApp', [
      'ngRoute',
      'ui.router',
    ])
    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

      $urlRouterProvider.otherwise('/jobs');

      // routes
      $stateProvider.state('jobs', {
        url: '/jobs',
        templateUrl: 'app/components/jobs/jobs.html',
        controller: 'jobsCtrl'
      }).state('personal', {
          url: '/personal',
          templateUrl: 'app/components/personal/personal.html',
          controller: 'personalCtrl'
      });

      $locationProvider.html5Mode(false);

  })
  .run(function () {
  });


})();
