/**
 * Created by Rynik on 30.01.2016.
 */
angular.module('FreelancerApp')
    .controller('myworksCtrl', function ($scope, myworksAPI, $log,$mdDialog, $mdMedia,Notification) {

        myworksAPI.getAllWorks().success(function (data) {
            $log.log(data);
            $scope.processedWorks = data.processedWorks;
            $scope.finishedWorks = data.finishedWorks;
            $scope.subscribedWorks = data.subscribedWorks;

            $scope.status = '  ';
            $scope.customFullscreen = $mdMedia('xs') || $mdMedia('sm');



        }).error(function () {
            Notification
                .error({
                    title: 'Error!',
                    message: 'Something went bad. Please try again.'
                });
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
                            console.log($scope.workers);

                            $mdDialog.show({
                                controller: DialogController,
                                templateUrl: 'app/components/myworks/workDetailsTabDialog.html',
                                parent: angular.element(document.body),
                                targetEvent: ev,
                                locals: {project:projectInfo ,customer: $scope.customer,workers:$scope.workers,
                                    workerInfo:$scope.workerInfo},
                                clickOutsideToClose:true
                            });



                        }
                    ).error(function(){
                            Notification
                                .error({
                                    title: 'Error!',
                                    message: 'Something went bad. Please try again.'
                                });
                        });
                }

            ).error(function(){
                    Notification
                        .error({
                            title: 'Error!',
                            message: 'Something went bad. Please try again.'
                        });
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


