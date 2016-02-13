angular
		.module('FreelancerApp')
		.controller(
				'developersCtrl',
				function($scope, developersService, $log, $http, Notification,
						$translate, $rootScope) {
					var filterOpen = false;
					var resourceLoadingCounter = 0;
					$scope.filter = {}
					$scope.developersLoading = true;
					$scope.filterButtonStyle = 'fa-angle-double-down';
					$scope.filter.payment = {};
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
									.instant('filter.open');
						else
							$scope.filter.tooltip.title = $translate
									.instant('filter.close');

						$scope.itemsPerPage[0].text = $translate
								.instant('pagination.item-5');
						$scope.itemsPerPage[1].text = $translate
								.instant('pagination.item-10');
						$scope.itemsPerPage[2].text = $translate
								.instant('pagination.item-15');

						initSelectTranslation($translate);
					});

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
								resourceLoadingCounter++;
								checkAndRestoreFilterData();
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

								resourceLoadingCounter++;
								checkAndRestoreFilterData();
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

					function checkAndRestoreFilterData() {
						if (resourceLoadingCounter == 2) {
							var data = getSavedStateData();
							if (data !== undefined) {
								$scope.filter.firstName = data.firstName;
								$scope.filter.lastName = data.lastName;
								$scope.filter.position = data.position;
								$scope.filter.payment.min = data.payment.min;
								$scope.filter.payment.max = data.payment.max;

								var savedTechs = data.selectedTechnologies;
								for (var i = 0; i < $scope.technologies.length; i++) {
									var technology = $scope.technologies[i];
									for (var j = 0; j < savedTechs.length; j++) {
										var savedTechnology = savedTechs[j];
										if (savedTechnology.id == technology.id)
											technology.ticked = true;
									}
								}

								var savedZones = data.selectedZones;
								for (var i = 0; i < $scope.timeZones.length; i++) {
									var zone = $scope.timeZones[i];
									for (var j = 0; j < savedZones.length; j++) {
										var savedZone = savedZones[j];
										if (savedZone.zone == zone.zone)
											zone.ticked = true;
									}
								}
							}

							initSelectTranslation($translate);
							$scope.doFilter();
						}
					}

					window.onbeforeunload = function() {
						var state = {};
						state.location = window.location.href;
						state.content = $scope.filter;
						localStorage.setItem("openTaskStateReloadedData", JSON
								.stringify(state));
					};
				});