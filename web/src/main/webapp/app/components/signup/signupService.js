'use strict';

angular.module('FreelancerApp').service('signupAPI', function() {
	var that = this;

	this.checkEmail = function($http, ngModel, email) {
		$http.get("/unreg/email", {
			params : {
				email : email
			}
		}).success(function(data, status, headers, config) {
			ngModel.$setValidity('emailAvailable', data);
		}).error(function(data, status, headers, config) {
		});
	}
});
