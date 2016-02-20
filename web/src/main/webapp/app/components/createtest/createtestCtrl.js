angular
		.module('FreelancerApp')
		.controller(
				'createtestCtrl',
				function($scope, createtestAPI, Notification, $log, $translate) {
					$scope.availableQuestions = [];

					$scope.chosenQuestions = [];

					$scope.openPage = function(page) {
						if (page == 'last')
							$scope.itemListStart = $scope.createTestMaxPage - 1;
						else 
							$scope.itemListStart = page;
						
						$scope.getQuestionsByTechId($scope.chosenTechID);
					};

					$scope.fillPagination = function(data) {
						$scope.pages = data;

						for (var page = 0; page < data.length; page++) {
							var item = data[page];
							if (item.first == 'current') {
								if (item.second > 3)
									$scope.showFirst = true;
								else
									$scope.showFirst = false;
								if (item.second + 4 >= $scope.createTestMaxPage)
									$scope.showLast = false;
								else
									$scope.showLast = true;
							}
						}
					};

					$scope.getQuestionsByTechId = function(id) {
						if (id != $scope.chosenTechID) {
							$scope.chosenQuestions = [];
							$scope.itemListStart = 0;
						}
						$scope.chosenTechID = id;
						createtestAPI
								.getQuestionsByTechId($scope.itemListStart, id)
								.success(
										function(data) {
											$scope.questions = data.items;
											$scope.createTestMaxPage = data.maxPage;
											$scope.availableQuestions = $scope.questions;
											$scope.availableQuestions.size = $scope.availableQuestions.length;

											for (var i = 0; i < $scope.chosenQuestions.length; i++) {
												for (var j = 0; j < $scope.availableQuestions.length; j++) {
													if ($scope.chosenQuestions[i].id == $scope.availableQuestions[j].id) {
														$scope.availableQuestions[j].checked = true;
														break;
													}
												}
											}

											$scope.fillPagination(data.pages);
										})
								.error(
										function() {
											Notification
													.error({
														title : $translate
																.instant('notification.error'),
														message : $translate
																.instant('notification.smth-wrong')
													});
										});
					};

					createtestAPI.getAllTests().success(function(data) {
						$scope.tests = data;
					}).error(
							function() {
								Notification.error({
									title : $translate
											.instant('notification.error'),
									message : $translate
											.instant('notification.smth-wrong')
								});
							});

					createtestAPI.getAllTechnologies().success(function(data) {
						$scope.technologies = data;
					}).error(
							function() {
								Notification.error({
									title : $translate
											.instant('notification.error'),
									message : $translate
											.instant('notification.smth-wrong')
								});
							});

					$scope.createTest = function() {
						if ($scope.chosenQuestions.length < 3) {
							Notification.error({
								title : $translate
										.instant('notification.error'),
								message : $translate
										.instant('createtest.err-questions')
							});
						} else {
							var testJSON = JSON.stringify($scope.test);
							var qIDs = [];
							for (var i = 0; i < $scope.chosenQuestions.length; i++) {
								qIDs.push($scope.chosenQuestions[i].id);
							}
							var questionsJSON = JSON.stringify(qIDs);

							createtestAPI
									.createTest(testJSON, questionsJSON)
									.success(
											function(data) {
												$scope.test.id = data;
												$scope.testReset();
												Notification
														.success({
															title : $translate
																	.instant('notification.success'),
															message : $translate
																	.instant('createtest.test-create-success')
														});
											})
									.error(
											function() {
												Notification
														.error({
															title : $translate
																	.instant('notification.error'),
															message : $translate
																	.instant('notification.smth-wrong')
														});
											});
						}
					};

					$scope.moveRight = function(id) {
						for (var i = 0; i < $scope.availableQuestions.length; i++) {
							if ($scope.availableQuestions[i].id == id) {
								$scope.chosenQuestions
										.push($scope.availableQuestions[i]);
								$scope.availableQuestions[i].checked = true;
								$scope.availableQuestions.size--;
							}
						}
					};

					$scope.moveLeft = function(id) {
						for (var i = 0; i < $scope.chosenQuestions.length; i++) {
							if ($scope.chosenQuestions[i].id == id) {
								for (var k = 0; k < $scope.availableQuestions.length; k++) {
									if ($scope.availableQuestions[k].id == id) {
										$scope.availableQuestions[k].checked = false;
										break;
									}
								}
								$scope.availableQuestions.size++;
								$scope.chosenQuestions.splice(i, 1);
							}
						}
					};

					$scope.testReset = function() {
						$scope.test = {};
						$scope.pages = [];
						$scope.chosenQuestions = [];
						$scope.availableQuestions = [];
						$scope.showLast = false;
					};

					var question = {};
					var answer = {
						name : '',
						correct : false
					};
					$scope.answers = [];
					$scope.answers.push(Object.assign({}, answer));

					$scope.addAnswerForm = function() {
						$scope.answers.push(Object.assign({}, answer));
					};

					$scope.deleteAnswerForm = function(id) {
						$scope.answers.splice(id, 1);
					};

					$scope.createQuestion = function() {
						if ($scope.answers.length < 2) {
							Notification.error({
								title : $translate
										.instant('notification.error'),
								message : $translate
										.instant('createtest.err-answers')
							});
							return;
						} else {
							var correctAnswers = 0;
							for (var i = 0; i < $scope.answers.length; i++) {
								if ($scope.answers[i].correct)
									correctAnswers++;
							}
							if (correctAnswers < 1) {
								Notification
										.error({
											title : $translate
													.instant('notification.error'),
											message : $translate
													.instant('createtest.err-correct-answers')
										});
								return;
							}
						}
						var questionJSON = JSON.stringify($scope.question);
						var answersJSON = JSON.stringify($scope.answers);

						createtestAPI
								.createQuestion(questionJSON, answersJSON)
								.success(
										function(data) {
											$scope.question.id = data;
											$scope.availableQuestions
													.push($scope.question);
											$scope.questionReset();
											for (var i = 0; i < $scope.chosenQuestions.length; i++) {
												if ($scope.chosenQuestions[i].id == $scope.delQuestion.id) {
													$scope.chosenQuestions
															.splice(i, 1);
													break;
												}
											}
											Notification
													.success({
														title : $translate
																.instant('notification.success'),
														message : $translate
																.instant('createtest.question-create-success')
													});
										})
								.error(
										function() {
											Notification
													.error({
														title : $translate
																.instant('notification.error'),
														message : $translate
																.instant('notification.smth-wrong')
													});
										});
					};

					$scope.questionReset = function() {
						$scope.answers = [];
						$scope.answers.push(Object.assign({}, answer));
						$scope.question = {};
					};

					$scope.setDelTest = function(test) {
						$scope.delTest = test;
					};

					$scope.deleteTest = function() {
						if ($scope.delTest == 'undefined') {
							Notification.error({
								title : $translate
										.instant('notification.error'),
								message : $translate
										.instant('notification.smth-wrong')
							});
							return;
						}
						var testJSON = JSON.stringify($scope.delTest);
						createtestAPI
								.deleteTest(testJSON)
								.success(
										function() {
											for (var i = 0; i < $scope.tests.length; i++) {
												if ($scope.tests[i].id == $scope.delTest.id) {
													$scope.tests.splice(i, 1);
													break;
												}
											}
											Notification
													.success({
														title : $translate
																.instant('notification.success'),
														message : $translate
																.instant('createtest.test-delete-success')
													});
										});
					};

					$scope.setDelQuestion = function(question) {
						$scope.delQuestion = question;
					};

					$scope.deleteQuestion = function() {
						if ($scope.delQuestion == 'undefined') {
							Notification.error({
								title : $translate
										.instant('notification.error'),
								message : $translate
										.instant('notification.smth-wrong')
							});
							return;
						}
						var questionJSON = JSON.stringify($scope.delQuestion);
						createtestAPI
								.deleteQuestion(questionJSON)
								.success(
										function() {
											for (var i = 0; i < $scope.allQuestions.length; i++) {
												if ($scope.allQuestions[i].id == $scope.delQuestion.id) {
													$scope.allQuestions.splice(
															i, 1);
													break;
												}
											}
											if ($scope.delQuestion.techId == $scope.chosenTechID) {
												for (var i = 0; i < $scope.availableQuestions.length; i++) {
													if ($scope.availableQuestions[i].id == $scope.delQuestion.id) {
														$scope.availableQuestions
																.splice(i, 1);
														break;
													}
												}
												for (var i = 0; i < $scope.chosenQuestions.length; i++) {
													if ($scope.chosenQuestions[i].id == $scope.delQuestion.id) {
														$scope.chosenQuestions
																.splice(i, 1);
														break;
													}
												}
											}
											Notification
													.success({
														title : $translate
																.instant('notification.success'),
														message : $translate
																.instant('createtest.question-delete-success')
													});
										});
					};

					$scope.sortField = undefined;
					$scope.reverse = false;

					$scope.sort = function(fieldName) {
						if ($scope.sortField === fieldName) {
							$scope.reverse = !$scope.reverse;
						} else {
							$scope.sortField = fieldName;
							$scope.reverse = false;
						}
					};

					$scope.isSortUp = function(fieldName) {
						return $scope.sortField === fieldName
								&& !$scope.reverse;
					};
					$scope.isSortDown = function(fieldName) {
						return $scope.sortField === fieldName && $scope.reverse;
					};

					// Question tab: pagination
					$scope.openQuestionPage = function(page) {
						if (page == 'last')
							$scope.questionItemListStart = $scope.maxPage - 1;
						else
							$scope.questionItemListStart = page;

						$scope
								.getQuestionsByTechIdForQuestTab($scope.questTabTechId);
					};

					$scope.fillQuestionTabPagination = function(data) {
						$scope.questionPages = data;

						for (var page = 0; page < data.length; page++) {
							var item = data[page];
							if (item.first == 'current') {
								if (item.second > 3)
									$scope.questTabShowFirst = true;
								else
									$scope.questTabShowFirst = false;
								if (item.second + 4 >= $scope.maxPage)
									$scope.questTabShowLast = false;
								else
									$scope.questTabShowLast = true;
							}
						}
					};

					$scope.getQuestionsByTechIdForQuestTab = function(id) {
						$scope.techIsChosen = true;
						createtestAPI
								.getQuestionsByTechId(
										$scope.questionItemListStart, id)
								.success(
										function(data) {
											$scope.allQuestions = data.items;
											$scope.maxPage = data.maxPage;
											$scope
													.fillQuestionTabPagination(data.pages);
										})
								.error(
										function() {
											Notification
													.error({
														title : $translate
																.instant('notification.error'),
														message : $translate
																.instant('notification.smth-wrong')
													});
										});
					}
				});