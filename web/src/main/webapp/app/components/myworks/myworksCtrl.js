/**
 * Created by Rynik on 30.01.2016.
 */
angular.module('FreelancerApp')
    .controller('myworksCtrl', function ($scope, myworksAPI, $log, $mdDialog, $mdMedia, Notification, $rootScope,$translate) {

        if ($rootScope.role == 'developer') {

            myworksAPI.getAllDeveloperWorks().success(function (data) {
                $scope.firstTitle = $translate.instant("myworks.tab-1-dev");

                $scope.firstWorks = data.subscribedWorks;
                $scope.secondWorks = data.processedWorks;
                $scope.thirdWorks = data.finishedWorks;
                $scope.notAcceptedWorks = data.notAcceptedWorks;


            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant("myworks.msg-error-title"),
                        message: $translate.instant("myworks.msg-error-descr")
                    });
            });
        } else {
            myworksAPI.getAllCustomerWorks().success(function (data) {
                $scope.firstTitle =  $translate.instant("myworks.tab-1-cust");
                $scope.firstWorks = data.availableWorks;
                $scope.secondWorks = data.inProgressWorks;
                $scope.thirdWorks = data.finishedWorks;
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant("myworks.msg-error-title"),
                        message: $translate.instant("myworks.msg-error-descr")
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
                                title: $translate.instant("myworks.msg-error-title"),
                                message: $translate.instant("myworks.msg-error-descr")
                            });
                    });

                myworksAPI.getDevWorkersByIdOrder(projectInfo.id).success(
                    function (dataWorkers) {
                        $scope.workers = dataWorkers.workers;
                        $scope.workerInfo = dataWorkers.workerInfo;

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
                                title: $translate.instant("myworks.msg-error-title"),
                                message: $translate.instant("myworks.msg-error-descr")
                            });
                    });
            }else{
                $scope.customer = {};
                $scope.customer.fname = $rootScope.name;
                $scope.customer.lname = $rootScope.lastName;

             myworksAPI.getCustWorkersByIdOrder(projectInfo.id).success(
                    function (dataWorkers) {
                        $scope.workers = dataWorkers.workers;

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
                                title: $translate.instant("myworks.msg-error-title"),
                                message: $translate.instant("myworks.msg-error-descr")
                            });
                    });
            }

        };

        $scope.showAcceptConfirm = function(ev,project) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.confirm()
                .title ($translate.instant("myworks.accept-confirm-title"))
                .textContent($translate.instant("myworks.accept-confirm-textContent"))
                .targetEvent(ev)
                .ok($translate.instant("myworks.accept-confirm-ok"))
                .cancel($translate.instant("myworks.accept-confirm-cancel"));
            $mdDialog.show(confirm).then(function() {
                myworksAPI.acceptOrdering(project.id).success(function(){
                    myworksAPI.getAllDeveloperWorks().success(function (data) {
                        $scope.firstTitle = $translate.instant("myworks.tab-1-dev");

                        $scope.firstWorks = data.subscribedWorks;
                        $scope.secondWorks = data.processedWorks;
                        $scope.thirdWorks = data.finishedWorks;
                        $scope.notAcceptedWorks = data.notAcceptedWorks;

                    }).error(function () {
                        Notification
                            .error({
                                title: $translate.instant("myworks.msg-error-title"),
                                message: $translate.instant("myworks.msg-error-descr")
                            });
                    });


                    Notification
                        .success({
                            title: $translate.instant("myworks.msg-success-title"),
                            message: $translate.instant("myworks.msg-success-descr-accepted-order")
                        });
                })
            }, function() {

            });
        };

        $scope.showRejectConfirm = function(ev,project) {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.confirm()
                .title($translate.instant("myworks.reject-confirm-title"))
                .textContent($translate.instant("myworks.reject-confirm-textContent"))
                .targetEvent(ev)
                .ok($translate.instant("myworks.reject-confirm-ok"))
                .cancel($translate.instant("myworks.reject-confirm-cancel"));
            $mdDialog.show(confirm).then(function() {
                myworksAPI.rejectOrdering(project.id).success(function(){
                    myworksAPI.getAllDeveloperWorks().success(function (data) {
                        $scope.firstTitle = 'Subscribed';

                        $scope.firstWorks = data.subscribedWorks;
                        $scope.secondWorks = data.processedWorks;
                        $scope.thirdWorks = data.finishedWorks;
                        $scope.notAcceptedWorks = data.notAcceptedWorks;

                    }).error(function () {
                        Notification
                            .error({
                                title: $translate.instant("myworks.msg-error-title"),
                                message: $translate.instant("myworks.msg-error-descr")
                            });
                    });


                    Notification
                        .success({
                            title: $translate.instant("myworks.msg-success-title"),
                            message: $translate.instant("myworks.msg-success-descr-rejected-order")
                        });
                })
            }, function() {

            });
        };

    });


function DialogController($scope, $mdDialog, project, customer, workers, workerInfo,$rootScope,myworksAPI,Notification,$translate) {
    $scope.project = project;
    $scope.customer = customer;
    $scope.workers = workers;
    $scope.workerInfo = workerInfo;
    $scope.role = $rootScope.role;


    if($scope.workerInfo == undefined){
        $scope.workerInfo = 'undefined';
    }

    if ($scope.project.endedDate == null) {
        $scope.project.endedDate =  $translate.instant("myworks.not-finished-yet");
    }

    $scope.cancel = function () {
        $mdDialog.cancel();
    };

    $scope.finishOrdering = function(order_id){
        myworksAPI.finishOrdering(order_id).success(function(){
            Notification
                .success({
                    title: $translate.instant("myworks.msg-success-title"),
                    message: $translate.instant("myworks.msg-success-descr-finished-order")
                });

            $scope.cancel();
             myworksAPI.getAllCustomerWorks().success(function (data) {
                 $scope.firstTitle =  $translate.instant("myworks.tab-1-cust");

                $scope.firstWorks = data.availableWorks;
                $scope.secondWorks = data.inProgressWorks;
                $scope.thirdWorks = data.finishedWorks;




            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant("myworks.msg-error-title"),
                        message: $translate.instant("myworks.msg-error-descr")
                    });
            });



        }).error(function(){
            Notification
                .error({
                    title: $translate.instant("myworks.msg-error-title"),
                    message: $translate.instant("myworks.msg-error-descr")
                });
        });

    }


}






