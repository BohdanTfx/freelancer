/**
 * Created by Rynik on 30.01.2016.
 */
angular.module('FreelancerApp')
    .controller('myworksCtrl', function ($scope, myworksAPI, $log, $mdDialog, $mdMedia, Notification, $rootScope) {

        if ($rootScope.role == 'developer') {

            myworksAPI.getAllDeveloperWorks().success(function (data) {
                $scope.firstTitle = 'Subscribed';

                $scope.firstWorks = data.subscribedWorks;
                $scope.secondWorks = data.processedWorks;
                $scope.thirdWorks = data.finishedWorks;
                $scope.notAcceptedWorks = data.notAcceptedWorks;



            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Something went bad. Please try again.'
                    });
            });
        } else {
            myworksAPI.getAllCustomerWorks().success(function (data) {
                $scope.firstTitle = 'Available';

                $scope.firstWorks = data.availableWorks;
                $scope.secondWorks = data.inProgressWorks;
                $scope.thirdWorks = data.finishedWorks;

            }).error(function () {
                Notification
                    .error({
                        title: 'Error!',
                        message: 'Something went bad. Please try again.'
                    });
            });


        }


        $scope.showTabDialog = function (ev, projectInfo) {
            if ($rootScope.role == 'developer') {
                myworksAPI.getCustomerById(projectInfo.customerId).success(
                    function (data) {
                        $scope.customer = data.cust;
                    }
                ).error(function () {
                        Notification
                            .error({
                                title: 'Error!',
                                message: 'Something went bad. Please try again.'
                            });
                    });

                myworksAPI.getDevWorkersByIdOrder(projectInfo.id).success(
                    function (dataWorkers) {
                        $scope.workers = dataWorkers.workers;
                        $scope.workerInfo = dataWorkers.workerInfo;

                        console.log($scope.workers);

                        $mdDialog.show({
                            controller: DialogController,
                            templateUrl: 'app/components/myworks/workDetailsTabDialog.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            locals: {
                                project: projectInfo, customer: $scope.customer, workers: $scope.workers,
                                workerInfo: $scope.workerInfo
                            },
                            clickOutsideToClose: true
                        });
                    }
                ).error(function () {
                        Notification
                            .error({
                                title: 'Error!',
                                message: 'Something went bad. Please try again.asdasdad!!!!!!!!!!!!!!!!!11111111'
                            });
                    });
            }else{
                myworksAPI.getCustWorkersByIdOrder(projectInfo.id).success(
                    function (dataWorkers) {
                        $scope.workers = dataWorkers.workers;

                        console.log($scope.workers);

                        $mdDialog.show({
                            controller: DialogController,
                            templateUrl: 'app/components/myworks/workDetailsTabDialog.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            locals: {
                                project: projectInfo, customer: $scope.customer, workers: $scope.workers,
                                workerInfo: $scope.workerInfo
                            },
                            clickOutsideToClose: true
                        });

                    }
                ).error(function () {
                        Notification
                            .error({
                                title: 'Error!',
                                message: 'Something went bad. Please try again.'
                            });
                    });
            }

        };

    });


function DialogController($scope, $mdDialog, project, customer, workers, workerInfo) {
    $scope.project = project;
    $scope.customer = customer;
    $scope.workers = workers;
    $scope.workerInfo = workerInfo

    if($scope.workerInfo == undefined){
        $scope.workerInfo = 'undefined';
    }

    if ($scope.project.endedDate == null) {
        $scope.project.endedDate = 'not finished yet';
    }

    $scope.cancel = function () {
        $mdDialog.cancel();
    };

}


