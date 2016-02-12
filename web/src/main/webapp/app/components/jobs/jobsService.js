'use strict';

angular
		.module('FreelancerApp')
		.service(
				'jobsAPI',
    function ($http) {
					var that = this;

					this.loadLimits = function($scope, $http) {
						$http.post("/user/orders/limits").success(
								function(data, status, headers, config) {
									$scope.payment = data;
									$scope.payment.hourly.options = {
										floor : $scope.payment.hourly.first,
										ceil : $scope.payment.hourly.second,
										disabled : true,
										translate : function(value) {
											return '$' + value;
										}
									};
									$scope.payment.fixed.options = {
										floor : $scope.payment.fixed.first,
										ceil : $scope.payment.fixed.second,
										disabled : true,
										translate : function(value) {
											return '$' + value;
										}
									};
								}).error(
								function(data, status, headers, config) {
									alert(data);
								});
                    };

					this.loadTechnologies = function($scope, $http) {
						$http.post("/user/technologies").success(
								function(data, status, headers, config) {
									$scope.tech = data;
								}).error(
								function(data, status, headers, config) {
									alert(data);
								});
                    };

					this.loadOrders = function($scope, $http, last) {
						var content = {};
						var title = $scope.filter.title;
						content.title = title === undefined
								|| title.length == 0 ? undefined : title;
						content.zone = [];
						content.technology = [];
						var zone = [];
						angular.forEach($scope.selectedZones, function(value,
								key) {
							zone.push(value.zone);
						});
						content.zone = zone === undefined || zone.length == 0 ? undefined
								: zone;

						if ($scope.payment) {
							if (!$scope.payment.hourly.options.disabled) {
								content.hourly = true;
								content.hmin = $scope.payment.hourly.first;
								content.hmax = $scope.payment.hourly.second;
							}
							if (!$scope.payment.fixed.options.disabled) {
								content.fixed = true;
								content.fmin = $scope.payment.fixed.first;
								content.fmax = $scope.payment.fixed.second;
							}
						}

						var technology = [];
						angular.forEach($scope.selectedTechs, function(value,
								key) {
							technology.push(value.id);
						});
						content.technology = technology === undefined
								|| technology.length == 0 ? undefined
								: technology;

						var pagination = {};
						pagination.start = $scope.itemListStart | 0;
						pagination.last = last;
						pagination.step = that.getStep($scope);

						var data = {};
						if (isNotEmpty(content))
							data.content = content;
						if (isNotEmpty(pagination))
							data.page = pagination;

						$scope.ordersLoading = true;
						$http
								.post(
										"/user/orders/filter",
										data,
										{
											headers : {
												'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
											}
										})
								.success(
										function(data, status, headers, config) {
											$scope.maxPage = data.maxPage;
											that.fillPagination(data.pages,
													$scope);
											that.fillOrders(data.items, $scope);

											$scope.ordersLoading = false;
										})
								.error(function(data, status, headers, config) {
									that.fillPagination(data.pages, $scope);
									that.fillOrders(data.items, $scope);
								});
                    };

        this.toComplain = function ($http, $scope, orderID, Notification) {
            $http.post("/user/orders/complain?orderID=" + orderID).success(
                function () {
                    Notification
                        .success({
                            title: 'Success!',
                            message: 'Succesfully complained. Thank you.'
                        });
                }).error(
                function () {
                    Notification
                        .error({
                            title: 'Error!',
                            message: 'Error while complaining order. Please try again.'
                        });
                });
        };

					this.fillPagination = function(data, $scope) {
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

					this.fillOrders = function(data, $scope) {
						$scope.orders = data;
                    };

					this.getStep = function($scope) {
						var localStep = localStorage
							.getItem("freelancerOrdersStep");
						if (localStep !== undefined && localStep != null)
							return localStep;
						else {
							var step = $scope.itesStep | 10;
							localStorage.setItem("freelancerOrdersStep", step);
							return step;
						}
					}
				});