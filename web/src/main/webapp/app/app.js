;
(function () {

    angular
        .module('FreelancerApp', [
            'ngRoute',
            'ui.router',
            'ngMaterial'
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
            }).state('tests', {
                url: '/tests',
                templateUrl: 'app/components/tests/tests.html',
                controller: 'testsCtrl'
            }).state('test', {
                url: '/tests/:testId',
                templateUrl: 'app/components/test/test.html',
                controller: 'testCtrl'
            });

            $locationProvider.html5Mode(false);

        })
        .run(function () {
        });


})();
