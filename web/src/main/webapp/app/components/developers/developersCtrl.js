var filterOpen = false;

angular.module('FreelancerApp').controller(
		'developersCtrl',
		function($scope, developersService, $log, $http, Notification) {
			$scope.filter = {}
			$scope.developersLoading = true;
			$scope.filterButtonStyle = 'fa-angle-double-down';

			$scope.filterToggle = function() {
				if (filterOpen) {
					$scope.filterState = '';
					$scope.filterButtonStyle = 'fa-angle-double-down';
					filterOpen = false;
				} else {
					$scope.filterState = 'in';
					$scope.filterButtonStyle = 'fa-angle-double-up';
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
				developersService.loadDevelopers($scope, $http);
			}

			$scope.changeStep = function() {
				localStorage.setItem("freelancerDevelopersStep",
						$scope.itesStep.number);
				developersService.loadDevelopers($scope, $http);
			}

			$scope.openPage = function(page) {
				if (page == 'last')
					developersService.loadDevelopers($scope, $http, 1);
				else {
					$scope.itemListStart = page;
					developersService.loadDevelopers($scope, $http);
				}
			}

			developersService.loadDevelopers($scope.filter, $scope.last,
					$scope.itemListStart, $scope.developersLoading,
					$scope.itesStep).success(
					function(data, status, headers, config) {
						alert(data);
					}).error(
					function(data, status, headers, config) {
						Notification.error({
							title : 'Error!',
							message : 'Some errors occurred'
									+ ' while loading developers!'
						});
					});
			developersService.loadTechnologies($scope, $http).success(
					function(data, status, headers, config) {
						$scope.tech = data;
					}).error(
					function(data, status, headers, config) {
						Notification.error({
							title : 'Error!',
							message : 'Some errors occurred'
									+ ' while loading technologies!'
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