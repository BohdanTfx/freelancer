angular.module('FreelancerApp')
    .controller('adminCtrl', function($scope, adminAPI ,$mdDialog){
        $scope.admin = {};
        $scope.admin.email = "";


        adminAPI.getStatisticsDevCust().success(function(data){
                $scope.custAmount = data.custAmount;
                $scope.devAmount = data.devAmount;

            alert(data.custAmount+'    '+data.devAmount);

            $scope.showDevCustStat();


            }).error(function(){
            alert(404);
        });


        $scope.showDevCustStat =  function(){
            var chart1 = {};
            chart1.type = "PieChart";
            chart1.data = [
                ['Component', 'cost'],
                ['Customers',  $scope.custAmount],
                ['Developers',  $scope.devAmount]
            ];

            chart1.options = {
                displayExactValues: true,
                width: 400,
                height: 200,
                is3D: true,
                chartArea: {left:10,top:10,bottom:0,height:"100%"}
            };

            $scope.chart = chart1;
       };

        $scope.sendAdminLinkToEmail = function(ev,email) {
            adminAPI.sendLinkToEmail(email);

            $mdDialog.show(
                $mdDialog.alert()
                    .parent(angular.element(document.querySelector('#popupContainer')))
                    .clickOutsideToClose(true)
                    .title("Attention")
                    .textContent('The message was sent')
                    .ok('Got it!')
                    .targetEvent(ev)
            );
        };


    });

