angular.module('FreelancerApp')
    .controller('adminCtrl', function($scope, adminAPI ,$mdDialog, $mdMedia){
        $scope.admin = {};
        $scope.admin.email = "";


        //angular.module("app", ["chart.js"]).controller("PieCtrl", function ($scope) {
            $scope.labels = ["Download Sales", "In-Store Sales", "Mail-Order Sales"];
            $scope.data = [300, 500, 100];
        //});



        $scope.sendAdminLinkToEmail = function(ev,email) {
            adminAPI.sendLinkToEmail(email);

            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title("Attention")
                    .textContent('The message was sent')
                    .ok('Got it!')
                    .targetEvent(ev)
            );
        };


    });

