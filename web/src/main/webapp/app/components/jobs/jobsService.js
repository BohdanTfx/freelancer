'use strict';

angular
		.module('FreelancerApp')
		.service(
				'jobsAPI',
				function($http) {
					var that = this;

					this.loadLimits = function() {
						return $http.post("/user/orders/limits");
					};

					this.loadTechnologies = function() {
						return $http.post("/user/technologies");
					};

					this.loadOrders = function($scope, last) {
						var pagination = {};
						pagination.start = $scope.itemListStart | 0;
						pagination.last = last;
						pagination.step = that.getStep($scope);

						var data = {};
						data.content = that.getFilterContent($scope.filter);
						if (isNotEmpty(pagination))
							data.page = pagination;

						$scope.ordersLoading = true;
						return $http
								.post(
										"/user/orders/filter",
										data,
										{
											headers : {
												'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
											}
										});
					};

					this.getFilterContent = function(filter) {
						var content = {};
						var title = filter.title;
						content.title = title === undefined
								|| title.length == 0 ? undefined : title;
						content.zone = [];
						content.technology = [];
						var zone = [];
						angular.forEach(filter.selectedZones, function(value,
								key) {
							zone.push(value.zone);
						});
						content.zone = zone === undefined || zone.length == 0 ? undefined
								: zone;

						if (filter.payment) {
							if (!filter.payment.hourly.options.disabled) {
								content.hourly = true;
								content.hmin = filter.payment.hourly.first;
								content.hmax = filter.payment.hourly.second;
							}
							if (!filter.payment.fixed.options.disabled) {
								content.fixed = true;
								content.fmin = filter.payment.fixed.first;
								content.fmax = filter.payment.fixed.second;
							}
						}

						var technology = [];
						angular.forEach(filter.selectedTechs, function(value,
								key) {
							technology.push(value.id);
						});
						content.technology = technology === undefined
								|| technology.length == 0 ? undefined
								: technology;

						if (isNotEmpty(content))
							return content;

						return undefined;
					}

					this.toComplain = function($http, $scope, orderID,
							Notification) {
						$http
								.post(
										"/user/orders/complain?orderID="
												+ orderID)
								.success(
										function() {
											Notification
													.success({
														title : 'Success!',
														message : 'Succesfully complained. Thank you.'
													});
										})
								.error(
										function() {
											Notification
													.error({
														title : 'Error!',
														message : 'Error while complaining order. Please try again.'
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