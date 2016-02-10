angular.module('FreelancerApp')
    .controller('createadminCtrl', function($scope, createadminAPI ,Notification){
        $scope.admin = {};
        $scope.admin.email = "";

        $scope.sendAdminLinkToEmail = function(ev,email) {
            createadminAPI.checkAvailableEmail(email).success(function(data){
               if(data.candidateEmailExists == true){
                  Notification.error({
                       title:"Error",
                       message:"The invitation has been already sent to this email."
                   });
                   return;
               }if(data.otherUserEmailExists == true){
                    Notification.error({
                        title:"Error",
                        message:"This user is already registered with another role."
                    });
                }else{
                    createadminAPI.sendLinkToEmail(email).success(function(){
                        Notification.success({
                            title:"Success",
                            message:"The invitation was sent successfully."
                        });
                    });
                }
            }).error(function(){
                Notification.error({
                    title:"Error",
                    message:"Something has happened wrong. Please try again later."
                });
            });
        };
    });

