var filterOpen = false;

angular.module('FreelancerApp').controller('jobsCtrl',
		function($scope, jobsAPI, $log, $http) {

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

			jobsAPI.loadOrders($scope, $http);
		});