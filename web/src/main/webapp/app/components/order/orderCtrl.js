angular
		.module('FreelancerApp')
		.controller(
				'orderCtrl',
				function($scope, orderService, $log, $http, Notification) {
					$scope.order = {};
					$scope.order['private'] = false;

					$scope.createOrder = function() {
						var self = this;
						var techValidation = orderService
								.validateTechnologies($scope.order.technologies);
						if (techValidation != 'ok') {
							Notification
									.error({
										title : 'Error!',
										message : $scope.TechnologyValidator[techValidation]
									});
							return;
						}
						orderService
								.createOrder($scope.order)
								.success(
										function(data, status, headers, config) {
											Notification
													.success({
														title : 'Order created!',
														message : 'Order "'
																+ $scope.order.title
																+ '" successfully added!'
													});
											self.resetInputs();
										})
								.error(
										function(data, status, headers, config) {
											Notification
													.error({
														title : 'Error!',
														message : 'Some errors occurred while creating order. Please reload page and try again.'
													});
										});
					}

					$scope.resetInputs = function() {
						$scope.order.title = "";
						$scope.order.descr = ""
						$scope.order.pay_type = "";
						$scope.order.payment = "";
						$scope.order.zone = $scope.getCurrentZone();
						$scope.order['private'] = false;
						angular.forEach($scope.technologies, function(value,
								key) {
							value['ticked'] = false;
						});
					}

					$scope.TechnologyValidator = {};
					$scope.TechnologyValidator.empty = 'Please, select at least one technology';
					$scope.TechnologyValidator.big = 'You have selected too many technologies. Please, be short.'

					$scope.getCurrentZone = function() {
						var offset = new Date().getTimezoneOffset(), o = Math
								.abs(offset);
						return Math.floor(o / 60);
					}

					$scope.timeZones = getTimeZones();

					orderService.loadTechnologies().success(
							function(data, status, headers, config) {
								$scope.technologies = data;
							}).error(function(data, status, headers, config) {
						alert(data);
					});

					$scope.filterTechnologies = function(query) {
						var results = query ? $scope.technologies
								.filter(createFilterFor(query)) : [];
						return results;
					}

					function createFilterFor(query) {
						var lowercaseQuery = angular.lowercase(query);
						return function filterFn(technology) {
							return (technology._lowername
									.indexOf(lowercaseQuery) != -1);
						};
					}
				})
		.directive(
				'ngOnlyNumber',
				function() {
					return {
						restrict : "AE",
						link : function(scope, elem, attr) {
							if (!$(elem).attr("min")) {
								$(elem).attr("min", 0);
							}
							function toIncreaseMaxLengthBy(elem) {
								var maxDecimalPoints = elem
										.data('maxDecimalPoints');
								return (1 + maxDecimalPoints);
							}
							var el = $(elem)[0];
							el.initMaxLength = elem.data('maxLength');
							el.maxDecimalPoints = elem.data('maxDecimalPoints');
							var checkPositive = function(elem, ev) {
								try {
									var el = $(elem)[0];
									if (el.value.indexOf('.') > -1) {
										if (ev.charCode >= 48
												&& ev.charCode <= 57) {
											if (el.value.indexOf('.') == el.value.length
													- toIncreaseMaxLengthBy(elem)) {
												if (el.selectionStart > el.value
														.indexOf('.')) {
													return false;
												} else {
													if (el.value.length == elem
															.data('maxLength')) {
														return false;
													} else {
														return true;
													}
												}
											} else {
												if (el.selectionStart <= el.value
														.indexOf('.')) {
													if (el.value.indexOf('.') == toIncreaseMaxLengthBy(elem)) {
														return false;
													}
												}
											}
										}
									} else {
										if (el.value.length == elem
												.data('maxLength')) {
											if (ev.charCode == 46) {
												if (typeof el.maxDecimalPoints === 'undefined') {
													return true;
												} else {
													if (el.selectionStart < el.value.length
															- el.maxDecimalPoints) {
														return false;
													}
													;
												}
												elem
														.data(
																'maxLength',
																el.initMaxLength
																		+ toIncreaseMaxLengthBy(elem));
												return true;
											} else if (ev.charCode >= 48
													&& ev.charCode <= 57) {
												return false;
											}
										}
										if (ev.charCode == 46) {
											if (el.selectionStart < el.value.length
													- elem
															.data('maxDecimalPoints')) {
												return false;
											} else {
												return true;
											}
										}
									}
									if (ev.charCode == 46) {
										if (el.value.indexOf('.') > -1) {
											return false;
										} else {
											return true;
										}
									}
									if ((ev.charCode < 48 || ev.charCode > 57)
											&& ev.charCode != 0) {
										return false;
									}
								} catch (err) {
								}
							}
							var change_maxlength = function(elem, ev) {
								try {
									var el = $(elem)[0];
									if (el.value.indexOf('.') > -1) {
										elem.data('maxLength', el.initMaxLength
												+ toIncreaseMaxLengthBy(elem));
									} else {
										if (elem.data('maxLength') == el.initMaxLength
												+ toIncreaseMaxLengthBy(elem)) {
											var dot_pos_past = el.selectionStart;
											el.value = el.value.substring(0,
													dot_pos_past);
										}
										elem
												.data('maxLength',
														el.initMaxLength);
									}
								} catch (err) {
								}
							}
							$(elem).on("keypress", function(event) {
								return checkPositive(elem, event);
							})
							$(elem).on("input", function(event) {
								return change_maxlength(elem, event);
							})
						}
					}
				});
;