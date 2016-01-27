'use strict';

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
					}

					this.loadOrders = function($scope, $http) {
						var content = {};
						var title = $scope.filter.title;
						content.title = title === undefined
								|| title.length == 0 ? undefined : title;
						content.zone = [];
						var zone = [];
						angular.forEach($scope.selectedZones, function(value,
								key) {
							zone.push(value.zone);
						});
						content.zone = zone === undefined || zone.length == 0 ? undefined
								: zone;

						var pagination = {};
						pagination.start = 0;
						pagination.step = 5;

						var data = {};
						if (isNotEmpty(content))
							data.content = content;
						if (isNotEmpty(pagination))
							data.page = pagination;
						$http
								.post(
										"/orders/filter",
										data,
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

function isNotEmpty(obj) {
	for ( var prop in obj)
		if (obj.hasOwnProperty(prop) && obj[prop] !== undefined)
			return true;
	return false;
}
