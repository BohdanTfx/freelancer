'use strict';

angular
		.module('FreelancerApp')
		.service(
				'orderService',
				function($http) {
					var that = this;
					$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

					this.loadTechnologies = function() {
						return $http.post("/user/technologies");
					}
				});
