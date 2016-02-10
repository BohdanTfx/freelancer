angular.module('FreelancerApp')
    .controller('createadminCtrl', function($scope, createadminAPI ,Notification){
        $scope.admin = {};
        $scope.admin.email = "";

        $scope.sendAdminLinkToEmail = function(ev,email) {

        createadminAPI.checkAvailableEmail(email).success(function(data){
           if(data == true){
               createadminAPI.sendLinkToEmail(email).success(function(){
                       Notification.success({
                           title:"Success",
                           message:"The invitation was sent successfully."
                       });
               });
           }else{
               Notification.error({
                   title:"Error",
                   message:"The invitation has been already sent to this email."
               });
           }
        })




        };




    });


