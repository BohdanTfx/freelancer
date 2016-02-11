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

					window.onbeforeunload = function() {
						var state = {};
						state.location = window.location.href;
						state.content = $scope.filter;
						localStorage.setItem("openTaskStateReloadedData", JSON
								.stringify(state));
					};

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

function getTimeZones() {
	return [ {
		zone : "-12",
		title : "-12 Baker island, Howland island",
		ticked : false
	}, {
		zone : "-11",
		title : "-11 American Samoa, Niue",
		ticked : false
	}, {
		zone : "-10",
		title : "-10 Hawaii",
		ticked : false
	}, {
		zone : "-9",
		title : "-9 Marquesas Islands, Gamblie Islands",
		ticked : false
	}, {
		zone : "-8",
		title : "-8 British Columbia, Mexico, California",
		ticked : false
	}, {
		zone : "-7",
		title : "-7 British Columbia, US Arizona",
		ticked : false
	}, {
		zone : "-6",
		title : "-6 Canada Saskatchewan, Costa Rica, Guatemala, Honduras",
		ticked : false
	}, {
		zone : "-5",
		title : "-5 Colombia, Cuba, Ecuador, Peru",
		ticked : false
	}, {
		zone : "-4",
		title : "-4 Venezuela, Bolivia, Brazil,	Barbados",
		ticked : false
	}, {
		zone : "-3",
		title : "-3 Newfoundland, Argentina, Chile",
		ticked : false
	}, {
		zone : "-2",
		title : "-2 South Georgia",
		ticked : false
	}, {
		zone : "-1",
		title : "-1 Capa Verde",
		ticked : false
	}, {
		zone : "0",
		title : "0 Ghana, Iceland, Senegal",
		ticked : false
	}, {
		zone : "1",
		title : "+1 Algeria, Nigeria, Tunisia",
		ticked : false
	}, {
		zone : "2",
		title : "+2 Ukraine, Zambia, Egypt",
		ticked : false
	}, {
		zone : "3",
		title : "+3 Belarus, Iraq, Iran",
		ticked : false
	}, {
		zone : "4",
		title : "+4 Armenia, Georgia, Oman",
		ticked : false
	}, {
		zone : "5",
		title : "+5 Kazakhstan, Pakistan, India",
		ticked : false
	}, {
		zone : "6",
		title : "+6 Ural, Bangladesh",
		ticked : false
	}, {
		zone : "7",
		title : "+7 Western Indonesai, Thailand",
		ticked : false
	}, {
		zone : "8",
		title : "+8 Hong Kong, China, Taiwan, Australia",
		ticked : false
	}, {
		zone : "9",
		title : "+9 Timor,Japan",
		ticked : false
	}, {
		zone : "10",
		title : "+10 New Guinea, Australia",
		ticked : false
	}, {
		zone : "11",
		title : "+11 Solomon Islands, Vanuatu",
		ticked : false
	}, {
		zone : "12",
		title : "+12 New zealand, Kamchatka, Kiribati",
		ticked : false
	} ];
}