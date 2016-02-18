angular.module('FreelancerApp')
    .controller('banCtrl', function ($scope, banAPI, Notification, $log, $translate) {
        $scope.openPage = function (page) {
            if (page == 'last')
                $scope.itemListStart = 'last';
            else {
                $scope.itemListStart = page;
                $scope.getComplainedOrders($scope.chosenTechID);
            }
        };

        $scope.fillPagination = function (data) {
            $scope.pages = data;

            for (var page = 0; page < data.length; page++) {
                var item = data[page];
                if (item.first == 'current') {
                    if (item.second > 3)
                        $scope.showFirst = true;
                    else
                        $scope.showFirst = false;
                    if (item.second + 4 >= $scope.maxPage)
                        $scope.showLast = false;
                    else
                        $scope.showLast = true;
                }
            }
        };

        $scope.getComplainedOrders = function () {
            banAPI.getComplainedOrders($scope.itemListStart).success(function (data) {
                //$scope.complained = data.items;
                $scope.complainedOrders = data;
                //$scope.fillPagination(data.pages);
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });
        };

        $scope.getComplainedOrders();

        $scope.getBanOrders = function () {
            banAPI.getBanOrders().success(function (data) {
                $scope.banOrders = data;
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });
        };

        $scope.getBanOrders();

        $scope.banOrder = function (id) {
            banAPI.banOrder(id).success(function () {
                var order;
                for (var i = 0; i < $scope.complainedOrders.length; i++) {
                    if ($scope.complainedOrders[i].id == id) {
                        order = $scope.complainedOrders[i];
                        $scope.complainedOrders.splice(i, 1);
                        break;
                    }
                }
                if ($scope.banOrders.length > 0) {
                    var i = 0;
                    while(i < $scope.banOrders.length && $scope.banOrders[i].complains > order.complains){
                        i++;
                    }
                    if(i >= $scope.banOrders.length) $scope.banOrders.push(order);
                    else $scope.banOrders.splice(i, 0, order);
                } else {
                    $scope.banOrders.push(order);
                }
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('banservice.ban-success')
                    });
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });
        };

        $scope.unbanOrder = function (id) {
            banAPI.unbanOrder(id).success(function () {
                var order;
                for (var i = 0; i < $scope.banOrders.length; i++) {
                    if ($scope.banOrders[i].id == id) {
                        order = $scope.banOrders[i];
                        $scope.banOrders.splice(i, 1);
                        break;
                    }
                }
                if ($scope.complainedOrders.length > 0) {
                    var i = 0;
                    while(i < $scope.complainedOrders.length && $scope.complainedOrders[i].complains > order.complains){
                        i++;
                    }
                    if(i >= $scope.complainedOrders.length) $scope.complainedOrders.push(order);
                    else $scope.complainedOrders.splice(i, 0, order);

                } else {
                    $scope.complainedOrders.push(order);
                }
                Notification
                    .success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('banservice.unban-success')
                    });
            }).error(function () {
                Notification
                    .error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
            });
        };
    });