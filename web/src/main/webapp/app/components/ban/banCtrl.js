angular
		.module('FreelancerApp')
		.controller(
				'banCtrl',
				function($scope, banAPI, Notification, $log, $translate) {
					$scope.isFirstLoading = true;
					$scope.showUnbannedFrom = function(page) {
						if (page == 'last')
							$scope.unbannedItemListStart = $scope.unbannedMaxPage - 1;
						else
							$scope.unbannedItemListStart = page;

						$scope.getComplainedOrders();
					};

					$scope.showBannedFrom = function(page) {
						if (page == 'last')
							$scope.bannedItemListStart = $scope.bannedMaxPage - 1;
						else
							$scope.bannedItemListStart = page;

						$scope.getBanOrders();
					};

					$scope.fillPagination = function(data, firstLabel,
							lastLabel, maxLabel, pagesLabel) {
						$scope[pagesLabel] = data;

						for (var page = 0; page < data.length; page++) {
							var item = data[page];
							if (item.first == 'current') {
								if (item.second > 3)
									$scope[firstLabel] = true;
								else
									$scope[firstLabel] = false;
								if (item.second + 4 >= $scope[maxLabel])
									$scope[lastLabel] = false;
								else
									$scope[lastLabel] = true;
							}
						}
					};

					$scope.getComplainedOrders = function() {
						banAPI
								.getOrders($scope.unbannedItemListStart, false)
								.success(
										function(data) {
											$scope.complainedOrders = data.items;
											$scope.unbannedMaxPage = data.maxPage;
											$scope.fillPagination(data.pages,
													"showUnbannedFirst",
													"showUnbannedLast",
													"unbannedMaxPage",
													"unbannedPages");

											if($scope.isFirstLoading){
												$scope.getBanOrders();
												$scope.isFirstLoading=false;
											}
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

					$scope.getComplainedOrders();

					$scope.getBanOrders = function() {
						banAPI
								.getOrders($scope.bannedItemListStart, true)
								.success(function(data) {
									$scope.banOrders = data.items;
									$scope.bannedMaxPage = data.maxPage;
									$scope.fillPagination(data.pages,
											"showBannedFirst",
											"showBannedLast",
											"bannedMaxPage",
											"bannedPages");
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

					$scope.setBanningOrder = function(id) {
						$scope.banningOrder = id;
					}

					$scope.banOrder = function() {
                        var id = $scope.banningOrder;
                        banAPI
								.banOrder(id)
								.success(
										function() {
											var order;
											for (var i = 0; i < $scope.complainedOrders.length; i++) {
												if ($scope.complainedOrders[i].id == id) {
													order = $scope.complainedOrders[i];
													$scope.complainedOrders
															.splice(i, 1);
													break;
												}
											}
											if ($scope.banOrders.length > 0) {
												var i = 0;
												while (i < $scope.banOrders.length
														&& $scope.banOrders[i].complains > order.complains) {
													i++;
												}
												if (i >= $scope.banOrders.length)
													$scope.banOrders
															.push(order);
												else
													$scope.banOrders.splice(i,
															0, order);
											} else {
												$scope.banOrders.push(order);
											}
											Notification
													.success({
														title : $translate
																.instant('notification.success'),
														message : $translate
																.instant('banservice.ban-success')
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

					$scope.unbanOrder = function(id) {
						banAPI
								.unbanOrder(id)
								.success(
										function() {
											var order;
											for (var i = 0; i < $scope.banOrders.length; i++) {
												if ($scope.banOrders[i].id == id) {
													order = $scope.banOrders[i];
													$scope.banOrders.splice(i,
															1);
													break;
												}
											}
											if ($scope.complainedOrders.length > 0) {
												var i = 0;
												while (i < $scope.complainedOrders.length
														&& $scope.complainedOrders[i].complains > order.complains) {
													i++;
												}
												if (i >= $scope.complainedOrders.length)
													$scope.complainedOrders
															.push(order);
												else
													$scope.complainedOrders
															.splice(i, 0, order);

											} else {
												$scope.complainedOrders
														.push(order);
											}
											Notification
													.success({
														title : $translate
																.instant('notification.success'),
														message : $translate
																.instant('banservice.unban-success')
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
				});