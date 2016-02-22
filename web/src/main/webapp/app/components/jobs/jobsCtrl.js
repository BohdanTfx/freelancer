angular
		.module('FreelancerApp')
		.controller(
				'jobsCtrl',
				function($scope, jobsAPI, $log, $http, Notification,
						$translate, $rootScope, $location) {
					var filterOpen = false;
					var resourceLoadingCounter = 0;
					$scope.filter = {};
					$scope.hourly = {};
					$scope.ordersLoading = true;
					$scope.filterButtonStyle = 'fa-angle-double-down';
					$scope.filter.tooltip = {};
					$scope.filter.tooltip.title = $translate
							.instant('filter.open');
					$scope.filter.tooltip.locked = true;
					$scope.isComp = false;

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

					$scope.setOrderID = function(orderID) {
						$scope.compOrderID = orderID;
					};

					$scope.complain = function() {
						jobsAPI
								.toComplain($scope.compOrderID)
								.success(
										function() {
											$scope.isComp = true;
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
						configUrlParamters();

						jobsAPI
								.loadOrders($scope)
								.success(
										function(data, status, headers, config) {
											$scope.maxPage = data.maxPage;
											jobsAPI.fillPagination(data.pages,
													$scope);
											jobsAPI.fillOrders(data.items,
													$scope);

											$scope.ordersLoading = false;
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
					};

					$scope.resetFilter = function() {
						$scope.filter.title = "";
						angular.forEach($scope.tech, function(value) {
							value.ticked = false;
						});
						angular.forEach($scope.timeZones, function(value) {
							value.ticked = false;
						});
						$scope.filter.payment.hourly.options.disabled = true;
						$scope.filter.payment.fixed.options.disabled = true;

						$scope.filter.payment.hourly.first = $scope.filter.payment.hourly.options.floor;
						$scope.filter.payment.hourly.second = $scope.filter.payment.hourly.options.ceil;
						$scope.filter.payment.fixed.first = $scope.filter.payment.fixed.options.floor;
						$scope.filter.payment.fixed.second = $scope.filter.payment.fixed.options.ceil;

						initSelectTranslation($translate);
					};

					$scope.changeStep = function() {
						localStorage.setItem("freelancerOrdersStep",
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

					jobsAPI
							.loadLimits()
							.success(function(data, status, headers, config) {
								$scope.filter.payment = data;
								$scope.filter.payment.hourly.options = {
									floor : $scope.filter.payment.hourly.first,
									ceil : $scope.filter.payment.hourly.second,
									disabled : true,
									translate : function(value) {
										return '$' + value;
									}
								};

								$scope.filter.payment.fixed.options = {
									floor : $scope.filter.payment.fixed.first,
									ceil : $scope.filter.payment.fixed.second,
									disabled : true,
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

					jobsAPI
							.loadTechnologies()
							.success(function(data, status, headers, config) {
								$scope.tech = data;
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
								$scope.filter.title = data.title;
								$scope.filter.payment = data.payment;
								try {
									$scope.filter.payment.hourly.options.translate = function(
											value) {
										return '$' + value;
									}
								} catch (e) {
									$log.debug(e);
								}
								try {
									$scope.filter.payment.fixed.options.translate = function(
											value) {
										return '$' + value;
									}
								} catch (e) {
									$log.debug(e);
								}

								var savedTechs = data.selectedTechs;
								for (var i = 0; i < $scope.tech.length; i++) {
									var technology = $scope.tech[i];
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

						var parameter = $location.search()['title'];
						if (parameter != null)
							$scope.filter.title = parameter;
						parameter = $location.search()['zone'];
						if (parameter != null) {
							var zoneParam = parameter.split(",");
							for (var zoneId = 0; zoneId < $scope.timeZones.length; zoneId++)
								for (var paramZoneId = 0; paramZoneId < zoneParam.length; paramZoneId++)
									if ($scope.timeZones[zoneId].zone == zoneParam[paramZoneId]) {
										$scope.timeZones[zoneId].ticked = true;
										break;
									}
						}
						parameter = $location.search()['technology'];
						if (parameter != null) {
							var techParam = parameter.split(",");
							for (var techId = 0; techId < $scope.tech.length; techId++)
								for (var paramTechId = 0; paramTechId < techParam.length; paramTechId++) {
									if ($scope.tech[techId].id == techParam[paramTechId]) {
										$scope.tech[techId].ticked = true;
										break;
									}
								}
						}

						parameter = $location.search()['hmin'];
						if (parameter != null) {
							$scope.filter.payment.hourly.first = parameter;
							$scope.filter.payment.hourly.options.disabled = false;
						}
						parameter = $location.search()['hmax'];
						if (parameter != null)
							$scope.filter.payment.hourly.second = parameter;
						parameter = $location.search()['fmin'];
						if (parameter != null) {
							$scope.filter.payment.fixed.options.disabled = false;
							$scope.filter.payment.fixed.first = parameter;
						}
						parameter = $location.search()['fmax'];
						if (parameter != null)
							$scope.filter.payment.fixed.second = parameter;

						return true;
					}

					function configUrlParamters() {
						var filterData = jobsAPI
								.getFilterContent($scope.filter);
						delete filterData.ban;
						delete filterData.sortOrderField;
						delete filterData.hourly;
						delete filterData.fixed;

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
				});