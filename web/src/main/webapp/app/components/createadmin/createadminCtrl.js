angular.module('FreelancerApp')
    .controller('createadminCtrl', function($scope, createadminAPI ,Notification,$translate){
        $scope.admin = {};
        $scope.admin.email = "";

        $scope.sendAdminLinkToEmail = function(ev,email) {
            createadminAPI.checkAvailableEmail(email).success(function(data){
               if(data.candidateEmailExists == true){
                  Notification.error({
                       title:$translate.instant("createadmin.error-title"),
                       message:$translate.instant("createadmin.error-invitation-already-sended")
                   });
                   return;
               }if(data.otherUserEmailExists == true){
                    Notification.error({
                        title:$translate.instant("createadmin.error-title"),
                        message:$translate.instant("createadmin.error-already-user")
                    });
                }else{
                    createadminAPI.sendLinkToEmail(email).success(function(){
                        Notification.success({
                            title:$translate.instant("createadmin.succes-title"),
                            message:$translate.instant("createadmin.succes-sended")
                        });
                    });
                }
            }).error(function(){
                Notification.error({
                    title:$translate.instant("createadmin.error-title"),
                    message:$translate.instant("createadmin.error-general")
                });
            });
        };
    });

