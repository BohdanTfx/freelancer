'use strict';

angular.module('FreelancerApp').factory('banAPI', function($http, config) {
	var dataFactory = {};

	dataFactory.getOrders = function(itemListStart, banned) {
		var pagination = {};
		pagination.start = itemListStart | 0;
		pagination.step = 10;

		var data = {};
		data.page = pagination;
		data.content = {};
		data.content.ban = banned;
		data.content.complains = 0;
		data.content.sortOrderField = 'complains';
		return $http.post('/admin/orders', data, {
			'Content-Type' : 'application/x-www-form-urlencoded'
		});
	};

	dataFactory.banOrder = function(orderId) {
		return $http.post('/admin/order/ban?orderId=' + orderId);
	};

	dataFactory.unbanOrder = function(orderId) {
		return $http.post('/admin/order/unban?orderId=' + orderId);
	};
	return dataFactory;
});
