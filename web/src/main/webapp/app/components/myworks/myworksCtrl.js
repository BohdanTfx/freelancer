/**
 * Created by Rynik on 30.01.2016.
 */
angular.module('FreelancerApp')
    .controller('myworksCtrl', function ($scope, myworksAPI, $log,$mdDialog, $mdMedia) {

        myworksAPI.getAllWorks().success(function (data) {
            $log.log(data);
            $scope.processedWorks = data.processedWorks;
            $scope.finishedWorks = data.finishedWorks;
            $scope.subscribedWorks = data.subscribedWorks;

            $scope.status = '  ';
            $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');


        }).error(function () {
            alert(404);
        });

        $scope.sortField = undefined;
        $scope.reverse = false;

        $scope.sort = function (fieldName) {
            if ($scope.sortField === fieldName) {
                $scope.reverse = !$scope.reverse;
            } else {
                $scope.sortField = fieldName;
                $scope.reverse = false;
            }
        };

        $scope.isSortUp = function (fieldName) {
            return $scope.sortField === fieldName && !$scope.reverse;
        };
        $scope.isSortDown = function (fieldName) {
            return $scope.sortField === fieldName && $scope.reverse;
        };

        $scope.showTabDialog = function(ev,projectInfo) {
            myworksAPI.getCustomerById(projectInfo.customerId).success(
                function (data){
                    $scope.customer = data.cust;
                    console.log("getCustById");
                }
            ).error(function(){
                    alert(404);
                });

            console.log('$scope.customer: '+$scope.customer.id);
             $mdDialog.show({
                controller: DialogController,
                templateUrl: 'app/components/myworks/workDetailsTabDialog.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                locals: {project:projectInfo ,customer: $scope.customer},
                clickOutsideToClose:true
            })
                .then(function(answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function() {
                    $scope.status = 'You cancelled the dialog.';
                });
        };
    });


angular.module('FreelancerApp').filter('dateFormat', function ($filter) {
    return function (input) {
        if (input == null) {
            return "";
        }
        var _date = $filter('date')(new Date(input), 'MMM dd yyyy');
        return _date;
    };
});

function DialogController($scope, $mdDialog, project,customer) {
    $scope.project = project;
    $scope.customer = customer;

    console.log('ProjectID: '+$scope.project.id+"     customerID: "+$scope.customer.id);

    if($scope.project.endedDate==null){
        $scope.project.endedDate = 'not finished yet';
    }

    $scope.cancel = function() {
        $mdDialog.cancel();
    };

}


