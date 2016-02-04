'use strict';

angular
		.module('FreelancerApp')
		.service(
				'orderService',
				function($http) {
					var self = this;

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

					this.createOrder = function(order) {
						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
							}
						}
						order.technologies = self.convertOrderTechnologies(
								order.technologies).join();
						return $http.post("/cust/order/create", order, config);
					}

					this.convertOrderTechnologies = function(technologies) {
						var techs = [];
						angular.forEach(technologies, function(value, key) {
							techs.push(value['id']);
						});

						return techs;
					}
				});
