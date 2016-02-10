angular.module('FreelancerApp')
    .controller('statisticsCtrl', function($scope, statisticsAPI ,$mdDialog){
        $scope.admin = {};
        $scope.admin.email = "";


        $scope.sendAdminLinkToEmail = function(ev,email) {
            statisticsAPI.sendLinkToEmail(email);

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


        //PieChart
        ///////////////////////////////////////////////////////////////////////////
        statisticsAPI.getStatisticsDevCust().success(function(data){
                $scope.custAmount = data.custAmount;
                $scope.devAmount = data.devAmount;

            //alert(data.custAmount+'    '+data.devAmount);

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
        ///////////////////////////////////////////////////////////////////////////


        //LineChart
        ///////////////////////////////////////////////////////////////////////////


        statisticsAPI.getStatisticsCreationOrders().success(function(data){

            console.log(data);
            $scope.orderValues = data.orderValues;
            $scope.listDays = data.listDays;
            $scope.listMonth = data.listMonth;



            $scope.showOrdersStat($scope.orderValues, $scope.listDays,$scope.listMonth);

        }).error(function(){
            alert(404);
        });


        $scope.showOrdersStat = function(orderValues, listDays,listMonth) {
             $scope.chartObject = {
                "type": "LineChart",
                "displayed": false,
                "data": {

                    "cols": [
                        {
                            "id": "day",
                            "label": "Days",
                            "type": "number"
                        },
                        {
                            "id": "Order-id",
                            "label": "orders",
                            "type": "number"
                        },

                    ],

                    "rows": [
                        {"c": [{"v": 29, "f": listMonth[29]+": " + listDays[29]}, {"v": orderValues[29]}]},
                        {"c": [{"v": 28, "f": listMonth[28]+": " + listDays[28]}, {"v": orderValues[28]}]},
                        {"c": [{"v": 27, "f": listMonth[27]+": "+ listDays[27]}, {"v": orderValues[27]}]},
                        {"c": [{"v": 26, "f": listMonth[26]+": "+ listDays[26]}, {"v": orderValues[26]}]},
                        {"c": [{"v": 25, "f": listMonth[25]+": " + listDays[25]}, {"v": orderValues[25]}]},
                        {"c": [{"v": 24, "f": listMonth[24]+": "+ listDays[24]}, {"v": orderValues[24]}]},
                        {"c": [{"v": 23, "f": listMonth[23]+": " + listDays[23]}, {"v": orderValues[23]}]},
                        {"c": [{"v": 22, "f": listMonth[22]+": " + listDays[22]}, {"v": orderValues[22]}]},
                        {"c": [{"v": 21, "f": listMonth[21]+": " + listDays[21]}, {"v": orderValues[21]}]},
                        {"c": [{"v": 20, "f": listMonth[20]+": " + listDays[20]}, {"v": orderValues[20]}]},
                        {"c": [{"v": 19, "f": listMonth[19]+": " + listDays[19]}, {"v": orderValues[19]}]},
                        {"c": [{"v": 18, "f": listMonth[18]+": " + listDays[18]}, {"v": orderValues[18]}]},
                        {"c": [{"v": 17, "f": listMonth[17]+": " + listDays[17]}, {"v": orderValues[17]}]},
                        {"c": [{"v": 16, "f": listMonth[16]+": " + listDays[16]}, {"v": orderValues[16]}]},
                        {"c": [{"v": 15, "f": listMonth[15]+": "+ listDays[15]}, {"v": orderValues[15]}]},
                        {"c": [{"v": 14, "f": listMonth[14]+": " + listDays[14]}, {"v": orderValues[14]}]},
                        {"c": [{"v": 13, "f": listMonth[13]+": " + listDays[13]}, {"v": orderValues[13]}]},
                        {"c": [{"v": 12, "f": listMonth[12]+": "+ listDays[12]}, {"v": orderValues[12]}]},
                        {"c": [{"v": 11, "f": listMonth[11]+": " + listDays[11]}, {"v": orderValues[11]}]},
                        {"c": [{"v": 10, "f": listMonth[10]+": " + listDays[10]}, {"v": orderValues[0]}]},
                        {"c": [{"v": 9, "f": listMonth[9]+": " + listDays[9]}, {"v": orderValues[9]}]},
                        {"c": [{"v": 8, "f": listMonth[8]+": " + listDays[8]}, {"v": orderValues[8]}]},
                        {"c": [{"v": 7, "f": listMonth[7]+": " + listDays[7]}, {"v": orderValues[7]}]},
                        {"c": [{"v": 6, "f": listMonth[6]+": " + listDays[6]}, {"v": orderValues[6]}]},
                        {"c": [{"v": 5, "f": listMonth[5]+": " + listDays[5]}, {"v": orderValues[5]}]},
                        {"c": [{"v": 4, "f": listMonth[4]+": " + listDays[4]}, {"v": orderValues[4]}]},
                        {"c": [{"v": 3, "f": listMonth[3]+": " + listDays[3]}, {"v": orderValues[3]}]},
                        {"c": [{"v": 2, "f": listMonth[2]+": " + listDays[2]}, {"v": orderValues[2]}]},
                        {"c": [{"v": 1, "f": listMonth[1]+": " + listDays[1]}, {"v": orderValues[1]}]},
                        {"c": [{"v": 0, "f": listMonth[0]+": " + listDays[0]}, {"v": orderValues[0]}]},

                    ]
                },
                "options": {
                    "title": "Orders per day",
                    "isStacked": "true",
                    "fill": 20,
                    "displayExactValues": true,
                    "vAxis": {
                        "title": "Orders unit",
                        "gridlines": {
                            "count": 10
                        }
                    },
                    "hAxis": {
                        "title": "Day"
                    },
                    "allowHtml": true,
                    "tooltip": {
                        "isHtml": false
                    }
                },
                "view": {"columns": [0, 1]}
            }

        }
        ///////////////////////////////////////////////////////////////////////////


    });

