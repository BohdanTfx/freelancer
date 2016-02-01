'use strict';

angular
		.module('FreelancerApp')
		.service(
				'signupAPI',
				function() {
					var that = this;

					this.checkEmail = function($http, ngModel, email) {
						$http.get("/user/email", {
							params : {
								email : email
							}
						}).success(function(data, status, headers, config) {
							ngModel.$setValidity('emailAvailable', data);
						}).error(function(data, status, headers, config) {
						});
					}

					this.createUser = function($http, user) {
						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8;'
							}
						}
						$http.post("/user/create", user, config).success(
								function(data, status, headers, config) {
									alert(data);
								}).error(
								function(data, status, headers, config) {
									alert(data);
								});
					}
				});
