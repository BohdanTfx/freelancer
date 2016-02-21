angular.module('FreelancerApp')
    .controller('technologiesCtrl', function($scope, technologiesAPI ,Notification,$translate,createtestAPI){
        $scope.technology = {};
        $scope.technology.techName = "";

        createtestAPI.getAllTechnologies().success(function (data) {
            $scope.technologies = data;
        }).error(function () {
            Notification
                .error({
                    title: $translate.instant('notification.error'),
                    message: $translate.instant('notification.smth-wrong')
                });
        });

        $scope.addTechnology = function(ev,name){
            technologiesAPI.addTechnology(name).success(function(){

                createtestAPI.getAllTechnologies().success(function (data) {
                    $scope.technologies = data;
                }).error(function () {
                    Notification
                        .error({
                            title: $translate.instant('notification.error'),
                            message: $translate.instant('notification.smth-wrong')
                        });
                });

                Notification.success({
                    title:$translate.instant('technologies.success'),
                    message:$translate.instant('technologies.success-descr-add')
                });
            });
        }

        $scope.deleteTechnology = function(ev,id){
            technologiesAPI.deleteTechnology(id).success(function(){

                createtestAPI.getAllTechnologies().success(function (data) {
                    $scope.technologies = data;
                }).error(function () {
                    Notification
                        .error({
                            title: $translate.instant('notification.error'),
                            message: $translate.instant('notification.smth-wrong')
                        });
                });

                Notification.success({
                    title:$translate.instant('technologies.success'),
                    message:$translate.instant('technologies.success-descr-rem')

                });
            });
        }




    });


