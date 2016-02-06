'use strict';

angular
		.module('FreelancerApp')
		.service(
				'developersService',
				function($http) {
					var self = this;

					this.loadTechnologies = function() {
						return $http.post("/user/technologies");
					}

					this.loadDevelopers = function(filter, last, itemListStart,
							developersLoading,itesStep) {
						var content = {};
						content.fullname = filter.fullname === undefined
								|| filter.fullname.length == 0 ? undefined
								: filter.fullname;
						content.position = filter.position === undefined
								|| filter.position.length == 0 ? undefined
								: filter.position;

						content.zone = [];
						var zone = [];
						angular.forEach(filter.selectedZones, function(value,
								key) {
							zone.push(value.zone);
						});
						content.zone = zone === undefined || zone.length == 0 ? undefined
								: zone;

						content.technology = [];
						var technology = [];
						angular.forEach(filter.selectedTechnologies, function(
								value, key) {
							technology.push(value.id);
						});
						content.technology = technology === undefined
								|| technology.length == 0 ? undefined
								: technology;

						var pagination = {};
						pagination.start = itemListStart | 0;
						pagination.last = last;
						pagination.step = self.getStep(itesStep);

						var data = {};
						if (isNotEmpty(content))
							data.content = content;
						if (isNotEmpty(pagination))
							data.page = pagination;

						developersLoading = true;
						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
							}
						};
						return $http.get("/user/developers/filter", data,
								config);
					}

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
					}

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