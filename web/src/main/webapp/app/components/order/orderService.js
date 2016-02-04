'use strict';

angular
		.module('FreelancerApp')
		.service(
				'orderService',
				function($http) {
					var self = this;
					$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

					this.loadTechnologies = function() {
						return $http.post("/user/technologies");
					}

					this.validateTechnologies = function(selectedTechnologies) {
						if (selectedTechnologies.length == 0)
							return 'empty';
						if (selectedTechnologies.length < 3)
							return 'small';
						if (selectedTechnologies.length > 15)
							return 'big';
						return 'ok';
					}
				});
