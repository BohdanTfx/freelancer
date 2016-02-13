angular
		.module('FreelancerApp')
		.controller(
				'jobsCtrl',
				function($scope, jobsAPI, $log, $http, Notification,
						$translate, $rootScope) {
					var filterOpen = false;
					$scope.filter = {};
					$scope.hourly = {};
					$scope.ordersLoading = true;
					$scope.filterButtonStyle = 'fa-angle-double-down';
					$scope.filter.tooltip = {};
					$scope.filter.tooltip.title = $translate
							.instant('filter.open');
					$scope.filter.tooltip.locked = true;

					$scope.filterToggle = function() {
						if (filterOpen) {
							$scope.filterState = '';
							$scope.filterButtonStyle = 'fa-angle-double-down';
							$scope.filter.tooltip.title = $translate
									.instant('filter.open');
							$scope.filter.tooltip.locked = true;
							filterOpen = false;
						} else {
							$scope.filterState = 'in';
							$scope.filterButtonStyle = 'fa-angle-double-up';
							$scope.filter.tooltip.title = $translate
									.instant('filter.close');
							$scope.filter.tooltip.locked = false;
							filterOpen = true;
						}
					}

					$scope.setOrderID = function(orderID) {
						$scope.compOrderID = orderID;
					};

					$scope.complain = function() {
						jobsAPI.toComplain($http, $scope, $scope.compOrderID,
								Notification);
					};

					$scope.itemsPerPage = [ {
						number : 5,
						text : $translate.instant('pagination.item-5')
					}, {
						number : 10,
						text : $translate.instant('pagination.item-10')
					}, {
						number : 15,
						text : $translate.instant('pagination.item-15')
					} ];

					$rootScope.$on('$translateChangeSuccess', function() {
						if (filterOpen)
							$scope.filter.tooltip.title = $translate
									.instant('filter.close');
						else
							$scope.filter.tooltip.title = $translate
									.instant('filter.open');

						$scope.itemsPerPage[0].text = $translate
								.instant('pagination.item-5');
						$scope.itemsPerPage[1].text = $translate
								.instant('pagination.item-10');
						$scope.itemsPerPage[2].text = $translate
								.instant('pagination.item-15');

						initSelectTranslation($translate);
					});

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

					function initSelectTranslation($translate) {
						$('#timeZonesSelect button[ng-if="helperStatus.all"]')
								.html(
										'&#x2714 '
												+ $translate
														.instant('developers.'
																+ 'time-zones-select.select-all'));
						$('#timeZonesSelect button[ng-if="helperStatus.none"]')
								.html(
										'&times '
												+ $translate
														.instant('developers.'
																+ 'time-zones-select.select-none'));
						$('#timeZonesSelect button[ng-if="helperStatus.reset"]')
								.html(
										'<i class="fa fa-rotate-right"></i> '
												+ $translate
														.instant('developers.'
																+ 'time-zones-select.reset'));
						$('#timeZonesSelect input.inputFilter').html(
								$translate.instant('developers.'
										+ 'time-zones-select.search'));
						$('#timeZonesSelect > span.multiSelect > button').html(
								$translate.instant('developers.'
										+ 'time-zones-select.empty')
										+ '<span class="caret"></span>');

						$(
								'#technologiesSelect button[ng-if="helperStatus.all"]')
								.html(
										'&#x2714 '
												+ $translate
														.instant('developers.'
																+ 'technologies-select.select-all'));
						$(
								'#technologiesSelect button[ng-if="helperStatus.none"]')
								.html(
										'&times '
												+ $translate
														.instant('developers.'
																+ 'technologies-select.select-none'));
						$(
								'#technologiesSelect button[ng-if="helperStatus.reset"]')
								.html(
										'<i class="fa fa-rotate-right"></i> '
												+ $translate
														.instant('developers.'
																+ 'technologies-select.reset'));
						$('#technologiesSelect input.inputFilter').html(
								$translate.instant('developers.'
										+ 'technologies-select.search'));

						// here is where the magic happens
						$('#technologiesSelect > span.multiSelect > button')
								.html(
										$translate.instant('developers.'
												+ 'technologies-select.empty')
												+ '<span class="caret"></span>');
					}
				});