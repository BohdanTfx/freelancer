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

                    myworksAPI.getWorkersByIdOrder(projectInfo.id).success(
                        function (dataWorkers){
                          $scope.workers = dataWorkers.workers;
                            $scope.workerInfo = dataWorkers.workerInfo;

                            //alert('dataWorkers.workerInfo: '+dataWorkers.workerInfo);

                            $mdDialog.show({
                                controller: DialogController,
                                templateUrl: 'app/components/myworks/workDetailsTabDialog.html',
                                parent: angular.element(document.body),
                                targetEvent: ev,
                                locals: {project:projectInfo ,customer: $scope.customer,workers:$scope.workers,
                                    workerInfo:$scope.workerInfo},
                                clickOutsideToClose:true
                            })
                                .then(function(answer) {
                                    $scope.status = 'You said the information was "' + answer + '".';
                                }, function() {
                                    $scope.status = 'You cancelled the dialog.';
                                });



                        }
                    ).error(function(){
                            alert(404);
                        });
                }

            ).error(function(){
                    alert(404);
                });

        };

     });


function DialogController($scope, $mdDialog, project,customer,workers,workerInfo) {
    $scope.project = project;
    $scope.customer = customer;
    $scope.workers = workers;
    $scope.workerInfo = workerInfo;

   if($scope.project.endedDate==null){
        $scope.project.endedDate = 'not finished yet';
    }

    $scope.cancel = function() {
        $mdDialog.cancel();
    };

}


