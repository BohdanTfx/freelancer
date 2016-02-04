angular.module('FreelancerApp')
    .controller('adminCtrl', function($scope, adminAPI){
        $scope.admin = {};
        $scope.admin.email = "";




        $scope.sendNewAdminEmail =  function(email){
            adminAPI.createAdmin(email);

        }







    });

