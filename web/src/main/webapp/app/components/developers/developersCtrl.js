var filterOpen = false;

angular
		.module('FreelancerApp')
		.controller(
				'developersCtrl',
				function($scope, developersService, $log, $http, Notification) {
					$scope.filter = {}
					$scope.developersLoading = true;
					$scope.filterButtonStyle = 'fa-angle-double-down';
					$scope.filter.payment = {};
					$scope.filter.tooltip = {};
					$scope.filter.tooltip.title = 'Open filter';
					$scope.filter.tooltip.locked = true;

					var data = getSavedStateData();
					if (data !== undefined)
						$scope.filter = data;

					$scope.filterToggle = function() {
						if (filterOpen) {
							$scope.filterState = '';
							$scope.filterButtonStyle = 'fa-angle-double-down';
							$scope.filter.tooltip.title = 'Open filter';
							$scope.filter.tooltip.locked = true;
							filterOpen = false;
						} else {
							$scope.filterState = 'in';
							$scope.filterButtonStyle = 'fa-angle-double-up';
							$scope.filter.tooltip.title = 'Close filter';
							$scope.filter.tooltip.locked = false;
							filterOpen = true;
						}
					}

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

					$scope.itesStep = $scope.itemsPerPage[developersService
							.getStep($scope) / 5 - 1];

					$scope.timeZones = getTimeZones();

					$scope.doFilter = function() {
						// var filterData = developersService.getFilterContent(
						// $scope.filter, $scope.filter.payment.min,
						// $scope.filter.payment.max);
						// var queryParams = transformToUrlQuery(filterData);
						// var newUrl = getURLWithoutParameters();
						// if (queryParams.length > 0) {
						// newUrl += '?' + queryParams;
						// if (window.location.href != newUrl)
						// window.history.pushState('data', 'title',
						// newUrl);
						// }

						developersService.loadDevelopers($scope.filter,
								$scope.last, $scope.itemListStart,
								$scope.developersLoading, $scope.itesStep,
								$scope.filter.payment.min,
								$scope.filter.payment.max).success(
								function(data, status, headers, config) {
									$scope.maxPage = data.maxPage;
									developersService.fillPagination(
											data.pages, $scope);
									$scope.developers = data.items;

									$scope.developersLoading = false;
								}).error(
								function(data, status, headers, config) {
									Notification.error({
										title : 'Error!',
										message : 'Some errors occurred'
												+ ' while loading developers!'
									});
								});
					}

					$scope.changeStep = function() {
						localStorage.setItem("freelancerDevelopersStep",
								$scope.itesStep.number);
						$scope.doFilter();
					}

					$scope.openPage = function(page) {
						if (page == 'last') {
							$scope.last = 1;
							$scope.doFilter();
						} else {
							$scope.itemListStart = page;
							$scope.last = undefined;
							$scope.doFilter();
						}
					}

					developersService
							.loadTechnologies($scope, $http)
							.success(function(data, status, headers, config) {
								$scope.technologies = data;
							})
							.error(
									function(data, status, headers, config) {
										Notification
												.error({
													title : 'Error!',
													message : 'Some errors occurred'
															+ ' while loading technologies! Please, try again.'
												});
									});
					$scope.doFilter();
					developersService
							.loadPaymentLimits()
							.success(function(data, status, headers, config) {
								$scope.filter.payment = {};
								$scope.filter.payment.min = data.min;
								$scope.filter.payment.max = data.max;

								$scope.filter.payment.options = {
									floor : data.min,
									ceil : data.max,
									translate : function(value) {
										return '$' + value;
									}
								};
							})
							.error(
									function(data, status, headers, config) {
										Notification
												.error({
													title : 'Error!',
													message : 'Some errors occurred'
															+ ' while loading payment limits! Please, try again.'
												});
									});
				});