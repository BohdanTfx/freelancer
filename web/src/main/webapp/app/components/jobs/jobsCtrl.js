angular.module('FreelancerApp')
	.controller('jobsCtrl', function ($scope, jobsAPI, $log) {

    /*    jobsAPI.getAllJobs().success(function(data){
            $log.log(data);
            $scope.orders = data;
                    });

        $scope.typeIncludes = [];

        $scope.includeType = function(type) {
            var i = $.inArray(type, $scope.typeIncludes);
            if (i > -1) {
                $scope.typeIncludes.splice(i, 1);
            } else {
                $scope.typeIncludes.push(type);
            }
        }

        $scope.orderFilter = function(order) {
            if ($scope.typeIncludes.length > 0) {
                if ($.inArray(order.payType, $scope.typeIncludes) < 0)
                    return;
            }

            return order;
        }
*/
	});