angular.module('FreelancerApp')
    .controller('createadminCtrl', function($scope, createadminAPI ,$mdDialog){
        $scope.admin = {};
        $scope.admin.email = "";

        $scope.sendAdminLinkToEmail = function(ev,email) {
        alert('sendAdminLinkToEmail');
            //createadminAPI.sendLinkToEmail(email);
            //
            //$mdDialog.show(
            //    $mdDialog.alert()
            //        .parent(angular.element(document.querySelector('#popupContainer')))
            //        .clickOutsideToClose(true)
            //        .title("Attention")
            //        .textContent('The message was sent')
            //        .ok('Got it!')
            //        .targetEvent(ev)
            //);
        };




    });


