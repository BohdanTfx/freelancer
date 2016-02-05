 angular.module('FreelancerApp')
    .controller('personalCtrl',['$scope', '$http','personalAPI', '$log', '$rootScope','$mdDialog', function($scope, $http, personalAPI, $log, $rootScope, $mdDialog){
        var devTemp, techsTemp, contTemp, custTemp;

        $scope.newPassword = '';
        $scope.confirmPassword = '';
        $scope.password = '';
        $scope.email='';

        if($rootScope.role =='developer'){
            personalAPI.getDevPersonal().success(function(data) {
                $scope.user = data.dev;
                if ($scope.user.regDate != 'underfined') {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }

                //check for empty Json
                if(typeof data.techs != 'underfined'){
                    $scope.techs = data.techs;
                }
                if(typeof data.contacts != 'underfined'){
                    $scope.cont = data.contacts;
                }
                if(typeof data.allTechs != 'underfined'){
                    $scope.allTechs = data.allTechs;
                }

                //check for empty image
                if(typeof $scope.user.imgUrl =='underfined'){
                    $scope.img = 'images/profile/default_logo.jpg';
                } else {
                    $scope.img = $scope.user.imgUrl;
                }
                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);
            });
        }

        $scope.hide = false;
        $scope.editClass = 'editClass';

        $scope.enableEditor = function() {
            $scope.editClass = '';
            $scope.hide = true;

            if($rootScope.role =='developer') {
                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);
            }
            if($rootScope.role == 'customer'){
                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);
            }
        };

        $scope.disableEditor = function() {
            $scope.hide = false;
            if($rootScope.role =='developer') {
                $scope.user = devTemp;
                $scope.techs = techsTemp;
                $scope.contact = contTemp;
            }
            if($rootScope.role == 'customer'){
                $scope.user = custTemp;
                $scope.contact = contTemp;
            }
            $scope.editClass = 'editClass';
        };

        $scope.save = function() {
            var devJson, techsJson, contactJson;
            $scope.editClass = 'editClass';
            $scope.hide = false;

            if($rootScope.role =='developer') {
                devJson = angular.toJson($scope.user);
                techsJson = angular.toJson($scope.techs);
                contactJson = angular.toJson($scope.contact);

                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);

                personalAPI.sendDevData(devJson, techsJson, contactJson).success(function (data){
                    $log.log(data);
                    $scope.result = data;
                }).error(function () {
                });
            }
            if($rootScope.role == 'customer') {
                var custJson;
                custJson = angular.toJson($scope.user);
                contactJson = angular.toJson($scope.contact);
                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);

                personalAPI.sendCustData(custJson, contactJson).success(function (data){
                    $log.log(data);
                    $scope.result = data;
                }).error(function () {
                });
            }
        };

        if($rootScope.role == 'customer') {
            personalAPI.getCustPersonal().success(function (data) {
                var custTemp, contTemp;
                $log.log(data);
                $scope.user = data.cust;
                $scope.contact = data.cont;
                if ($scope.user.regDate != 'underfined') {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }

                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);

                if (typeof $scope.user.imgUrl == 'undefined') {
                    $scope.img = 'images/profile/default_logo.jpg';
                }
                else {
                    $scope.img = $scope.user.imgUrl;
                }

            });
        }

        function clone(obj) {
            if(obj === null || typeof(obj) !== 'object' || 'isActiveClone' in obj)
                return obj;

            var temp = obj.constructor(); // changed

            for(var key in obj) {
                if(Object.prototype.hasOwnProperty.call(obj, key)) {
                    obj['isActiveClone'] = null;
                    temp[key] = clone(obj[key]);
                    delete obj['isActiveClone'];
                }
            }
            return temp;
        }

        $scope.resetPassword = function(){
            $scope.newPassword = '';
            $scope.confirmPassword = '';
            $scope.password = '';
        };

        $scope.resetEmail = function(){
            $scope.email = '';
        };

        $scope.changePassword = function() {

            if($rootScope.role =='developer'){
                personalAPI.changeDevPassword($scope.password, $scope.newPassword).success(function (data){
                    $log.log(data);
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPassword = $scope.newPassword;
                    console.log($scope.confirmPasswordFlag);

                }).error(function (response) {
                    $scope.confirmPasswordFlag = false;
                    console.log($scope.confirmPasswordFlag);
                    $scope.error = 'Invalid credentials';
                });
            }

            if($rootScope.role == 'customer') {
                personalAPI.changeCustPassword($scope.password, $scope.newPassword).success(function (data){
                    $log.log(data);
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPassword = $scope.newPassword;
                    console.log($scope.confirmPasswordFlag);


                }).error(function (response) {
                    $scope.confirmPasswordFlag = false;
                    $scope.error = 'Invalid credentials';
                });
            }
        };

        $scope.confirmCode = '';

        $scope.confirmPhoneCode = function(){
            if($rootScope.role =='developer'){
                if($scope.confirmCode == $scope.user.confirmCode){
                    personalAPI.confirmCodeAndChangeDevPassword($scope.confirmPassword).success(function (data){
                        $log.log(data);
                        $scope.result = data;
                        $scope.confirmPhoneCode = true;

                    }).error(function (response) {
                        $scope.confirmPhoneCode = false;
                    });
                    $scope.showDialogConfirm();
                } else {
                    $scope.confirmPhoneCode = false;
                    $scope.showDialogConfirm();
                }
            }
            if($rootScope.role == 'customer'){
                if($scope.confirmCode == $scope.user.confirmCode){
                    personalAPI.confirmCodeAndChangeCustPassword($scope.confirmPassword).success(function (data){
                        $log.log(data);
                        $scope.result = data;
                        $scope.confirmPhoneCode = true;
                    }).error(function (response) {
                        $scope.confirmPhoneCode = false;
                    });
                    $scope.showDialogConfirm();
                } else {
                    $scope.confirmPhoneCode = false;
                    $scope.showDialogConfirm()
                }
            }
        };

        $scope.showTabDialog = function ($event){
            $mdDialog.show({
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/changePasswordTabDialog.html',
                controller: 'personalCtrl',
                onComplete: afterShowAnimation,
                locals: { confirmPasswordFlag: $scope.confirmPasswordFlag }
            });

            function afterShowAnimation(scope, element, options) {

            }
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.showDialogConfirm = function ($event){
            $mdDialog.show({
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/passwordChanged.html',
                controller: 'personalCtrl',
                onComplete: afterShowAnimation,
                locals: { confirmPasswordFlag: $scope.confirmPasswordFlag }
            });

            function afterShowAnimation(scope, element, options) {

            }
        };
        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.uploadFromDevice = function (e, callback) {
            var img = new Image();
            var canvas = document.createElement("canvas");
            console.log('upload image');
            var file = document.getElementById('file').files[0];
            img.src = URL.createObjectURL(file);
            var ctx = canvas.getContext("2d");
            img.onload = function () {
                ctx = canvas.getContext("2d");
                var maxWidth = 2000, // Max width for the image
                    ratio = 0,  // Used for aspect ratio
                    width = img.width,    // Current image width
                    height = img.height;  // Current image height

                if (width > maxWidth) {
                    ratio = maxWidth / width;   // get ratio for scaling image
                    height = height * ratio;    // Reset height to match scaled image
                    width = width * ratio;    // Reset width to match scaled image
                }
                canvas.width = width;
                canvas.height = height;
                ctx.fillStyle = '#fff';  /// set white fill style
                ctx.fillRect(0, 0, width, height);
                ctx.drawImage(this, 0, 0, width, height);
                var dataURL = canvas.toDataURL("image/jpg");
                var base = dataURL.replace(/^data:image\/(png|jpg);base64,/, "");

                console.log(base);
                //personalAPI.
                personalAPI.sendImage(base).success(function (data){
                    $log.log(data);
                    $scope.result = data;
                }).error(function () {
                    console.log('error image');
                });


            }
        };

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
    }]);
