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

					this.initSocial = function($http, $scope) {
						var linkedinVerifier = getUrlVars();
						if (linkedinVerifier !== undefined
								&& linkedinVerifier.oauth_verifier !== undefined) {
							$http.get("/user/signup/linkedin", {
								params : {
									verifier : linkedinVerifier.oauth_verifier
								}
							}).success(function(data, status, headers, config) {
								$scope.user.email = data.emailAddress;
								$scope.user.first_name = data.firstName;
								$scope.user.last_name = data.lastName;
								$scope.user.img_url = data.pictureUrl;
								$scope.signup = true;
								$scope.role = localStorage.getItem("role");
							}).error(function(data, status, headers, config) {
								alert('error ' + data);
							});
							return;
						}

						$http.get("/user/signup/social", {
							params : {
								callbackUrl : document.URL
							}
						}).success(function(data, status, headers, config) {
							$scope.linkedinUrl = data.linkedinUrl;
						}).error(function(data, status, headers, config) {
							alert('error ' + data);
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

function getUrlVars() {
	var vars = [], hash;
	var hashes = window.location.href.slice(
			window.location.href.indexOf('?') + 1).split('&');
	for (var i = 0; i < hashes.length; i++) {
		hash = hashes[i].split('=');
		vars.push(hash[0]);
		vars[hash[0]] = hash[1];
	}
	return vars;
}
