angular
		.module('FreelancerApp')
		.controller(
				'orderCtrl',
				function($scope, jobsAPI, $log, $http) {
					$scope.createOrder = function() {

					}

					$scope.getCurrentZone = function() {
						var offset = new Date().getTimezoneOffset(), o = Math
								.abs(offset);
						return Math.floor(o / 60);
					}

					$scope.timeZones = [
							{
								zone : "-12",
								title : "-12 Baker island, Howland island",
								ticked : false
							},
							{
								zone : "-11",
								title : "-11 American Samoa, Niue",
								ticked : false
							},
							{
								zone : "-10",
								title : "-10 Hawaii",
								ticked : false
							},
							{
								zone : "-9",
								title : "-9 Marquesas Islands, Gamblie Islands",
								ticked : false
							},
							{
								zone : "-8",
								title : "-8 British Columbia, Mexico, California",
								ticked : false
							},
							{
								zone : "-7",
								title : "-7 British Columbia, US Arizona",
								ticked : false
							},
							{
								zone : "-6",
								title : "-6 Canada Saskatchewan, Costa Rica, Guatemala, Honduras",
								ticked : false
							},
							{
								zone : "-5",
								title : "-5 Colombia, Cuba, Ecuador, Peru",
								ticked : false
							},
							{
								zone : "-4",
								title : "-4 Venezuela, Bolivia, Brazil,	Barbados",
								ticked : false
							},
							{
								zone : "-3",
								title : "-3 Newfoundland, Argentina, Chile",
								ticked : false
							},
							{
								zone : "-2",
								title : "-2 South Georgia",
								ticked : false
							},
							{
								zone : "-1",
								title : "-1 Capa Verde",
								ticked : false
							},
							{
								zone : "0",
								title : "0 Ghana, Iceland, Senegal",
								ticked : false
							},
							{
								zone : "1",
								title : "+1 Algeria, Nigeria, Tunisia",
								ticked : false
							},
							{
								zone : "2",
								title : "+2 Ukraine, Zambia, Egypt",
								ticked : false
							},
							{
								zone : "3",
								title : "+3 Belarus, Iraq, Iran",
								ticked : false
							},
							{
								zone : "4",
								title : "+4 Armenia, Georgia, Oman",
								ticked : false
							},
							{
								zone : "5",
								title : "+5 Kazakhstan, Pakistan, India",
								ticked : false
							},
							{
								zone : "6",
								title : "+6 Ural, Bangladesh",
								ticked : false
							},
							{
								zone : "7",
								title : "+7 Western Indonesai, Thailand",
								ticked : false
							},
							{
								zone : "8",
								title : "+8 Hong Kong, China, Taiwan, Australia",
								ticked : false
							}, {
								zone : "9",
								title : "+9 Timor,Japan",
								ticked : false
							}, {
								zone : "10",
								title : "+10 New Guinea, Australia",
								ticked : false
							}, {
								zone : "11",
								title : "+11 Solomon Islands, Vanuatu",
								ticked : false
							}, {
								zone : "12",
								title : "+12 New zealand, Kamchatka, Kiribati",
								ticked : false
							} ];
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