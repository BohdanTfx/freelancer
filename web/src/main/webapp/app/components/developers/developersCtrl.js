angular
		.module('FreelancerApp')
		.controller(
				'developersCtrl',
				function($scope, developersService, $log, $http, Notification,
						$translate, $rootScope, $location) {
					var filterOpen = false;
					var resourceLoadingCounter = 0;
					$scope.filter = {};
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

					$scope.itesStep = $scope.itemsPerPage[developersService
							.getStep($scope) / 5 - 1];

					$scope.timeZones = getTimeZones();

					$scope.doFilter = function() {
						configUrlParamters();

						developersService
								.loadDevelopers($scope.filter, $scope.last,
										$scope.itemListStart,
										$scope.developersLoading,
										$scope.itesStep,
										$scope.filter.payment.min,
										$scope.filter.payment.max)
								.success(
										function(data, status, headers, config) {
											$scope.maxPage = data.maxPage;
											developersService.fillPagination(
													data.pages, $scope);
											$scope.developers = data.items;

											$scope.developersLoading = false;
										})
								.error(
										function(data, status, headers, config) {
											Notification
													.error({
														title : $translate
																.instant('message.error'),
														message : $translate
																.instant('developers.loading.error.developers')
													});
										});
					};

					$scope.resetFilter = function() {
						$scope.filter.firstName = "";
						$scope.filter.lastName = "";
						$scope.filter.position = "";
						angular.forEach($scope.technologies, function(value) {
							value.ticked = false;
						});
						angular.forEach($scope.timeZones, function(value) {
							value.ticked = false;
						});
						$scope.filter.payment.options.disabled = true;

						$scope.filter.payment.min = $scope.filter.payment.options.floor;
						$scope.filter.payment.max = $scope.filter.payment.options.ceil;

						initSelectTranslation($translate);
					};

					$scope.changeStep = function() {
						localStorage.setItem("freelancerDevelopersStep",
								$scope.itesStep.number);
						$scope.doFilter();
					};

					$scope.openPage = function(page) {
						if (page == 'last') {
							$scope.last = 1;
							$scope.doFilter();
						} else {
							$scope.itemListStart = page;
							$scope.last = undefined;
							$scope.doFilter();
						}
					};

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
													title : $translate
															.instant('message.error'),
													message : $translate
															.instant('developers.loading.error.technologies')
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
													title : $translate
															.instant('message.error'),
													message : $translate
															.instant('developers.loading.error.payment.limits')
												});
									});

					function configUrlParamters() {
						var filterData = developersService.getFilterContent(
								$scope.filter, $scope.filter.payment.min,
								$scope.filter.payment.max);

						for ( var param in filterData)
							if (filterData.hasOwnProperty(param)
									&& filterData[param] !== undefined) {
								if (param == 'technology' || param == 'zone') {
									$location.search(param, filterData[param]
											.join());
								} else
									$location.search(param, filterData[param]);
							}
					}

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
							if (parseUrlParameters()) {
								initSelectTranslation($translate);
								$scope.doFilter();
								return;
							}

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

					function parseUrlParameters() {
						var vars = getUrlVars();
						if (vars.length < 1)
							return false;

						var parameter = $location.search()['name'];
						if (parameter != null)
							$scope.filter.firstName = parameter;
						parameter = $location.search()['last_name'];
						if (parameter != null)
							$scope.filter.lastName = parameter;
						parameter = $location.search()['position'];
						if (parameter != null)
							$scope.filter.position = parameter;
						parameter = $location.search()['zone'];
						if (parameter != null) {
							var zoneParam = parameter.split(",");
							for (var zoneId = 0; zoneId < $scope.timeZones.length; zoneId++)
								for (var paramZoneId = 0; paramZoneId < zoneParam.length; paramZoneId++)
									if ($scope.timeZones[zoneId] == zoneParam[paramZoneId]) {
										$scope.timeZones.ticked = true;
										break;
									}
						}
						parameter = $location.search()['technology'];
						if (parameter != null) {
							var techParam = parameter.split(",");
							for (var techId = 0; techId < $scope.technologies.length; techId++)
								for (var paramTechId = 0; paramTechId < techParam.length; paramTechId++)
									if ($scope.technologies[techId] == techParam[paramTechId]) {
										$scope.technologies.ticked = true;
										break;
									}
						}

						parameter = $location.search()['paymentMin'];
						if (parameter != null)
							$scope.filter.payment.min = parameter;
						parameter = $location.search()['paymentMax'];
						if (parameter != null)
							$scope.filter.payment.max = parameter;

						return true;
					}
				});