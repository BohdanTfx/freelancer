'use strict';

angular.module('FreelancerApp')
	.factory('jobsAPI', function ($http, config) {
		var urlBase = '/getall',
			dataFactory = {};

		dataFactory.getAllJobs = function () {
			return $http.get(urlBase);
		};



		return dataFactory;
	});
