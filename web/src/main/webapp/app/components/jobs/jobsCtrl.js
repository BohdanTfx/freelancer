var filterOpen = false;

angular.module('FreelancerApp').controller('jobsCtrl',
		function($scope, jobsAPI, $log, $http) {
			$scope.filter = {}

			$scope.filterToggle = function() {
				if (filterOpen) {
					$scope.filterState = '';
					filterOpen = false;
				} else {
					$scope.filterState = 'in';
					filterOpen = true;
				}
			}

			$scope.modernBrowsers = [ {
				name : "Opera",
				maker : "Opera Software",
				ticked : false
			}, {
				name : "Internet Explorer",
				maker : "Microsoft",
				ticked : true
			}, {
				name : "Firefox",
				maker : "Mozilla Foundation",
				ticked : false
			}, {
				name : "Safari",
				maker : "Apple",
				ticked : true
			}, {
				name : "Chrome",
				maker : "Google",
				ticked : true
			} ];

			$scope.doFilter = function() {
				jobsAPI.loadOrders($scope, $http);
			}

			jobsAPI.loadOrders($scope, $http);
			jobsAPI.loadLimits($scope, $http);

			$scope.rangeArray = [ {
				value : 0.2,
				name : 'Clock In'
			}, {
				value : 0.4,
				name : 'Start Break'
			}, {
				value : 0.6,
				name : 'End Break'
			}, {
				value : 0.8,
				name : 'Clock Out'
			} ]
			$scope.views = [ {
				zoom : 0.9,
				step : 1 / 40,
				// visible units for this view, first entry being the major unit
				units : [ {
					value : 1 / 10,
					// function to transform your value into labels | true:
					// value itself | false: none
					labeller : function(n) {
						return n * 10
					}
				}, {
					value : 1 / 20,
				} ]
			}, {
				zoom : 1.5,
				step : 1 / 80,
				units : [ {
					value : 1 / 20,
					labeller : function(n) {
						return n * 10
					}
				}, {
					value : 1 / 40,
				} ]
			} ];
		});