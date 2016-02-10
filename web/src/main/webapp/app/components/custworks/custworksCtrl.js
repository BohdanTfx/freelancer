/**
 * Created by Rynik on 30.01.2016.
 */
angular.module('FreelancerApp')
    .controller('custworksCtrl', function ($scope, custworksAPI, $log, $mdDialog, $mdMedia, Notification) {

        custworksAPI.getAllWorks().success(function (data) {
            $log.log(data);

            $scope.finishedWorks = data.finishedWorks;
            $scope.inProgressWorks = data.inProgressWorks;
            $scope.availableWorks = data.availableWorks;

        }).error(function () {
            Notification
                .error({
                    title: 'Error!',
                    message: 'Something went bad. Please try again.'
                });
        });

        $scope.sortField = undefined;
        $scope.reverse = false;

        $scope.showWorkDetailTabDialog = function (ev, projectInfo) {
            custworksAPI.getFollowers(projectInfo.id).success(
                function (data) {
                console.log(data);
                    $scope.followers = data;

                    $mdDialog.show({
                        controller: CustWorkDetailDialogController,
                        templateUrl: 'app/components/custworks/workDetail.html',
                        parent: angular.element(document.body),
                        targetEvent: ev,
                        locals: {
                            followers:  $scope.followers
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

                }
            ).error(function () {
                    Notification
                        .error({
                            title: 'Error!',
                            message: 'Something went bad. Please try again.'
                        });
                });

        };

    });


function CustWorkDetailDialogController($scope, $mdDialog,followers ) {
    $scope.followers = followers;

    $scope.cancel = function () {
        $mdDialog.cancel();
    };

}


/**
 * Created by Rynik on 10.02.2016.
 */
