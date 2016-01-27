'use strict';

// angular.module('FreelancerApp')
// .factory('jobsAPI', function ($http, config) {
// var urlBase = '/getall',
// dataFactory = {};
//
// dataFactory.getAllJobs = function () {
// return $http.get(urlBase);
// };
//
//
//
// return dataFactory;
// });

angular
		.module('FreelancerApp')
		.service(
				'jobsAPI',
				function() {
					var that = this;

					this.loadLimits = function($scope, $http) {
						$http.post("/orders/limits").success(
								function(data, status, headers, config) {
									$scope.payment = data;
								}).error(
								function(data, status, headers, config) {
									alert(data);
								});
					}

					this.loadOrders = function($scope, $http) {
						var content = {};
						content.title = $scope.filter.title;
						var pagination = {};
						pagination.start = 0;
						pagination.step = 5;

						$http
								.post(
										"/orders/filter",
										{
											content : content,
											page : pagination
										},
										{
											headers : {
												'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
											}
										})
								.success(
										function(data, status, headers, config) {
											that.fillPagination(data.pages,
													$scope);
											that.fillOrders(data.items, $scope);
										})
								.error(function(data, status, headers, config) {
									that.fillPagination(data.pages, $scope);
									that.fillOrders(data.items, $scope);
								});
					}

					this.fillPagination = function(data, $scope) {
						$scope.pages = data;
					}
					this.fillOrders = function(data, $scope) {
						$scope.orders = data;
					}
				});
