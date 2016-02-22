'use strict';

angular
		.module('FreelancerApp')
		.service(
				'developersService',
				function($http) {
					var self = this;

					this.loadTechnologies = function() {
						return $http.post("/user/technologies");
					};

					this.loadPaymentLimits = function() {
						return $http.get("/user/developers/payment/limits");
					};

					this.loadDevelopers = function(filter, last, itemListStart,
							developersLoading, itesStep, paymentMin, paymentMax) {

						var pagination = {};
						pagination.start = itemListStart | 0;
						pagination.last = last;
						pagination.step = self.getStep(itesStep);

						var data = {};
						data.content = self.getFilterContent(filter,
								paymentMin, paymentMax);
						if (isNotEmpty(pagination))
							data.page = pagination;

						developersLoading = true;
						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
							}
						};
						return $http.post("/user/developers/filter", data,
								config);
					};

					this.getFilterContent = function(filter, paymentMin,
							paymentMax) {
						var content = {};
						content.name = filter.firstName === undefined
								|| filter.firstName.length == 0 ? undefined
								: filter.firstName;
						content.last_name = filter.lastName === undefined
								|| filter.lastName.length == 0 ? undefined
								: filter.lastName;
						content.position = filter.position === undefined
								|| filter.position.length == 0 ? undefined
								: filter.position;

						content.zone = [];
						var zone = [];
						angular.forEach(filter.selectedZones, function(value,
								key) {
							zone.push(value.zone);
						});
						content.zone = zone.length == 0 ? undefined : zone;

						content.technology = [];
						var technology = [];
						angular.forEach(filter.selectedTechnologies, function(
								value, key) {
							technology.push(value.id);
						});
						content.technology = technology.length == 0 ? undefined
								: technology;

						content.paymentMin = paymentMin;
						content.paymentMax = paymentMax;

						if (isNotEmpty(content))
							return content;

						return undefined;
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

					this.getStep = function(itesStep) {
						var localStep = localStorage
								.getItem("freelancerDevelopersStep");
						if (localStep !== undefined && localStep != null)
							return localStep;
						else {
							var step = itesStep | 10;
							localStorage.setItem("freelancerDevelopersStep",
									step);
							return step;
						}
					}
				});