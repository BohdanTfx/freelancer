angular.module('FreelancerApp')
    .controller('personalCtrl',['$scope', '$http','personalAPI', '$log', '$rootScope','$mdDialog', 'Notification', function($scope, $http, personalAPI, $log, $rootScope, $mdDialog, Notification){
        var devTemp, techsTemp, contTemp, custTemp, adminTemp;

        $scope.newPassword = '';
        $scope.confirmPassword = '';
        $scope.password = '';
        $scope.newEmail = '';

        if($rootScope.role =='developer'){
            personalAPI.getDevPersonal().success(function(data) {
                $scope.user = data.dev;

                //check Registration date
                if ($scope.user.regDate != undefined) {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }

                //check send e-mail
                if($scope.user.sendEmail == undefined){
                    $scope.email = $scope.user.email;
                } else {
                    $scope.email = $scope.user.sendEmail;
                }

                //check for empty Json
                if(typeof data.techs != 'undefined'){
                    $scope.techs = data.techs;
                }
                if(typeof data.contacts != 'undefined'){
                    $scope.contact = data.contacts;
                }
                if(typeof data.allTechs != 'undefined'){
                    $scope.allTechs = data.allTechs;
                }

                //check for empty image
                if(typeof $scope.user.imgUrl == 'undefined'){
                    $scope.img = 'images/profile/no-image.png';
                } else {
                    $scope.img = $scope.user.imgUrl;
                }
                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);
            });
        }

        if($rootScope.role == 'customer') {
            personalAPI.getCustPersonal().success(function (data) {
                var custTemp, contTemp;
                $scope.user = data.cust;
                $scope.contact = data.cont;

                //check Registration date
                if ($scope.user.regDate != undefined) {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }

                //check send e-mail
                if($scope.user.sendEmail != undefined){
                    $scope.email = $scope.user.sendEmail;
                } else {
                    $scope.email = $scope.user.email;
                }

                //check for empty Json
                if(typeof data.contacts != 'undefined'){
                    $scope.contact = data.contacts;
                }

                //check for empty image
                if (typeof $scope.user.imgUrl == 'undefined') {
                    $scope.img = 'images/profile/no-image.png';
                }
                else {
                    $scope.img = $scope.user.imgUrl;
                }
                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);
            });
        }

        if($rootScope.role == 'admin'){
            personalAPI.getAdminPersonal().success(function (data){
                $scope.user = data;

                //check Registration date
                if ($scope.user.regDate != undefined) {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }

                //check for empty image
                if (typeof $scope.user.imgUrl == 'undefined') {
                    $scope.img = 'images/profile/no-image.png';
                }
                else {
                    $scope.img = $scope.user.imgUrl;
                }

                adminTemp = clone($scope.user);
            });

        }





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
                    $scope.result = data;
                    Notification.success({
                        title: 'Changes saved!',
                        message: 'All data is updated!'
                    });

                }).error(function () {
                    Notification.error({
                            title: 'Error!',
                            message: 'Something wrong with saved data. Try again!'
                        });
                });
            }
            if($rootScope.role == 'customer') {
                var custJson;
                custJson = angular.toJson($scope.user);
                contactJson = angular.toJson($scope.contact);
                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);

                personalAPI.sendCustData(custJson, contactJson).success(function (data){
                    $scope.result = data;
                    Notification.success({
                        title: 'Changes saved!',
                        message: 'All data is updated!'
                    });
                }).error(function () {
                    Notification.error({
                        title: 'Error!',
                        message: 'Something wrong with saved data. Try again!'
                    });
                });
            }

            if($rootScope.role == 'admin'){
                var adminJson;
                adminJson = angular.toJson($scope.user);
                adminTemp = clone($scope.user);
                $scope.user = adminTemp;

                personalAPI.sendAdminData(adminJson).success(function (data){
                    $scope.result = data;
                    Notification.success({
                        title: 'Changes saved!',
                        message: 'All data is updated!'
                    });
                }).error(function () {
                    Notification.error({
                        title: 'Error!',
                        message: 'Something wrong with saved data. Try again!'
                    });
                });
            }

        };


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
            if($rootScope.role == 'admin'){
                adminTemp = clone($scope.user);
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
            if($rootScope.role == 'admin'){
                $scope.user = adminTemp;
            }
            $scope.editClass = 'editClass';
        };

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
            $scope.newEmail = '';
        };

        $scope.changePassword = function() {
            $scope.confirmCode = '';
            if($rootScope.role =='developer'){
                personalAPI.changeDevPassword($scope.password, $scope.newPassword).success(function (data){
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPasswordTemp = clone($scope.newPassword);
                    $scope.showTabDialog();
                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Wrong old password. Try again!'
                    });
                });
            }

            if($rootScope.role == 'customer') {
                personalAPI.changeCustPassword($scope.password, $scope.newPassword).success(function (data){
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPasswordTemp = $scope.newPassword;
                    $scope.showTabDialog();

                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Wrong old password. Try again!'
                    });
                });
            }
            if($rootScope.role == 'admin'){
                personalAPI.changeAdminPassword($scope.password, $scope.newPassword).success(function (data){
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPasswordTemp = $scope.newPassword;
                    $scope.showTabDialog();

                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Wrong old password. Try again!'
                    });
                });
            }
        };

        $scope.confirmPhoneCode = function(){
            if($rootScope.role =='developer'){
                personalAPI.confirmCodeAndChangeDevPassword($scope.confirmPasswordTemp, $scope.confirmCode).success(function (data){
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    if($scope.confirmPasswordFlag == undefined){
                        $scope.showDialogConfirm();
                    } else {
                        $scope.confirmEmailDialog();
                    }

                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Wrong confirm code. Try again!'
                    });
                });
            }
            if($rootScope.role == 'customer'){
                personalAPI.confirmCodeAndChangeCustPassword($scope.confirmPasswordTemp, $scope.confirmCode).success(function (data){
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    $scope.showDialogConfirm();
                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Wrong confirm code. Try again!'
                    });
                });
            }

            if($rootScope.role == 'admin'){
                personalAPI.confirmCodeAndChangeAdminPassword($scope.confirmPasswordTemp, $scope.confirmCode).success(function (data){
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    $scope.showDialogConfirm();
                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Wrong confirm code. Try again!'
                    });
                });
            }
        };

        $scope.changeSendingEmail = function(){
            $scope.confirmCode = '';
            if($rootScope.role =='developer'){
                personalAPI.changeDevSendingEmail($scope.newEmail).success(function (data){
                    $scope.confirmEmailFlag = true;
                    $scope.newEmailTemp = clone($scope.newPassword);
                    $scope.showEmailDialog();

                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Something wrong with change email. Try again!'
                    });
                });
            }
            if($rootScope.role == 'customer'){
                personalAPI.changeCustSendingEmail($scope.newEmail).success(function (data){
                    $scope.result = data;
                    $scope.confirmEmailFlag = true;
                    $scope.newEmailTemp = clone($scope.newPassword);
                    $scope.showTabDialog();

                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Something wrong with change email. Try again!'
                    });
                });
            }

            if($rootScope.role == 'admin'){
                personalAPI.changeAdminSendingEmail($scope.newEmail).success(function (data){
                    $scope.result = data;
                    $scope.confirmEmailFlag = true;
                    $scope.newEmailTemp = clone($scope.newPassword);
                    $scope.showTabDialog();

                }).error(function (response) {
                    Notification.error({
                        title: 'Error!',
                        message: 'Something wrong with change email. Try again!'
                    });
                });
            }
        };

        $scope.showTabDialog = function ($event){
            $mdDialog.show({
                //parent: angular.element(document.body),
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/changePasswordDialog.html',
                controller: 'personalCtrl',
                clickOutsideToClose: true,
                onComplete: afterShowAnimation,
                locals: {confirmPasswordFlag: $scope.confirmPasswordFlag}
            });

            function afterShowAnimation(scope, element, options) {

            }
        };

        $scope.showEmailDialog = function ($event){
            $mdDialog.show({
                parent: angular.element(document.body),
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/changeEmailDialog.html',
                controller: 'personalCtrl',
                clickOutsideToClose: true,
                onComplete: afterShowAnimation,
                locals: {confirmPasswordFlag: $scope.confirmPasswordFlag}
            });

            function afterShowAnimation(scope, element, options) {

            }
        };

        $scope.confirmEmailDialog = function ($event){
            $mdDialog.show({
                parent: angular.element(document.body),
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/emailChanged.html',
                controller: 'personalCtrl',
                clickOutsideToClose: true,
                onComplete: afterShowAnimation,
                locals: {confirmPasswordFlag: $scope.confirmPasswordFlag}
            });

            function afterShowAnimation(scope, element, options) {

            }
        };


        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        $scope.showDialogConfirm = function ($event){
            $mdDialog.show({
                //parent: angular.element(document.body),
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/passwordChanged.html',
                controller: 'personalCtrl',
                onComplete: afterShowAnimation,
                locals: {flagConfirmPhoneCode: $scope.flagConfirmPhoneCode }
            });

            function afterShowAnimation(scope, element, options) {

            }
        };

        $scope.cancel = function() {
            $mdDialog.cancel();
        };

        //$scope.showUploadDialog = function ($event){
        //    $mdDialog.show({
        //        parent: angular.element(document.body),
        //        targetEvent: $event,
        //        scope: $scope,
        //        preserveScope: true,
        //        templateUrl: 'app/components/personal/uploadFoto.html',
        //        controller: 'personalCtrl',
        //        onComplete: afterShowAnimation,
        //        locals: {flagConfirmPhoneCode: $scope.flagConfirmPhoneCode }
        //    });
        //
        //    function afterShowAnimation(scope, element, options) {
        //
        //    }
        //};

        //$scope.$watch('files', function () {
        //    $scope.upload($scope.files);
        //});

        //$scope.files =['files'];
        //$scope.upload = function (files) {
        //    if (files && files.length) {
        //        for (var i = 0; i < files.length; i++) {
        //            var file = files[i];
        //            //$scope.uploadFromDevice(file);
        //            Upload.upload({
        //                url: 'upload/url',
        //                fields: {'username': $scope.username},
        //                file: file
        //            }).progress(function (evt) {
        //                //$scope.uploadFromDevice(evt);
        //                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
        //                console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
        //            }).success(function (data, status, headers, config) {
        //                console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
        //            });
        //        }
        //    }
        //};

        $scope.close = function() {
            $mdDialog.hide();
        };

        $scope.uploadFromDevice = function (e, callback) {
            var img = new Image();
            var canvas = document.createElement("canvas");
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

                    if($rootScope.role =='developer'){
                        personalAPI.sendDevImage(base).success(function (data){
                            $scope.result = data;
                            Notification.success({
                                title: 'Changes saved!',
                                message: 'The image is uploaded!'
                            });
                        }).error(function () {
                            Notification.error({
                                title: 'Error!',
                                message: 'Something wrong with upload image. Try again!'
                            });
                        });
                    }
                    if($rootScope.role =='customer'){
                        personalAPI.sendCustImage(base).success(function (data){
                            $scope.result = data;
                            Notification.success({
                                title: 'Changes saved!',
                                message: 'The image is uploaded!'
                            });
                        }).error(function () {
                            Notification.error({
                                title: 'Error!',
                                message: 'Something wrong with upload image. Try again!'
                            });
                        });
                    }
                    if($rootScope.role =='admin'){
                        personalAPI.sendAdminImage(base).success(function (data){
                            $scope.result = data;
                            Notification.success({
                                title: 'Changes saved!',
                                message: 'The image is uploaded!'
                            });
                        }).error(function () {
                            Notification.error({
                                title: 'Error!',
                                message: 'Something wrong with upload image. Try again!'
                            });
                        });
                    }
                }


        };

        $scope.loadTechs = function($scope, $http, $query) {
            return $http.post("/user/orders/tech").success(function(data) {
                var techs = data;
                return techs.filter(function (tech) {
                    return tech.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
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
