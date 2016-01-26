angular.module('FreelancerApp')
    .controller('testsCtrl', function ($scope, testsAPI, $log) {

     jobsAPI.getAllTests().success(function(data){
      $log.log(data);
      $scope.devQAs = data;
     });

    });