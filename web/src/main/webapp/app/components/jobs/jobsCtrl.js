var filterOpen = false;

angular.module('FreelancerApp')
		.controller(
				'jobsCtrl',
    function ($scope, jobsAPI, $log, $http, Notification) {
        $scope.filter = {};
					$scope.hourly = {};
					$scope.ordersLoading = true;
					$scope.filterButtonStyle = 'fa-angle-double-down';

					$scope.filterToggle = function() {
						if (filterOpen) {
							$scope.filterState = '';
							$scope.filterButtonStyle = 'fa-angle-double-down';
							filterOpen = false;
						} else {
							$scope.filterState = 'in';
							$scope.filterButtonStyle = 'fa-angle-double-up';
							filterOpen = true;
						}
                    };

        $scope.setOrderID = function (orderID) {
            $scope.compOrderID = orderID;
        };

        $scope.complain = function () {
            jobsAPI.toComplain($http, $scope, $scope.compOrderID, Notification);
        };

					$scope.itemsPerPage = [ {
						number : 5,
						text : "Show 5 items on page"
					}, {
						number : 10,
						text : "Show 10 items on page"
					}, {
						number : 15,
						text : "Show 15 items on page"
					} ];
					$scope.itesStep = $scope.itemsPerPage[jobsAPI
							.getStep($scope) / 5 - 1];

					$scope.timeZones = getTimeZones();

					$scope.doFilter = function() {
						jobsAPI.loadOrders($scope, $http);
                    };

					$scope.changeStep = function() {
						localStorage.setItem("freelancerOrdersStep",
								$scope.itesStep.number);
						jobsAPI.loadOrders($scope, $http);
                    };

					$scope.openPage = function(page) {
						if (page == 'last')
							jobsAPI.loadOrders($scope, $http, 1);
						else {
							$scope.itemListStart = page;
							jobsAPI.loadOrders($scope, $http);
						}
                    };

					jobsAPI.loadLimits($scope, $http);
					jobsAPI.loadOrders($scope, $http);
					jobsAPI.loadTechnologies($scope, $http);
				});