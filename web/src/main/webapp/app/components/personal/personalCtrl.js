angular.module('FreelancerApp')
    .controller('personalCtrl', ['$scope', '$http', 'personalAPI', '$log', '$rootScope', '$mdDialog', 'Notification', '$translate', function ($scope, $http, personalAPI, $log, $rootScope, $mdDialog, Notification, $translate) {
        var devTemp, techsTemp, contTemp, custTemp, adminTemp;
                
        $scope.newPassword = '';
        $scope.confirmPassword = '';
        $scope.password = '';
        $scope.newEmail = '';

        $scope.getDevPersonal = function () {
            personalAPI.getDevPersonal().success(function (data) {
                var random = Math.random();
                $scope.user = data.dev;

                if ($scope.user.lang == undefined) {
                    $scope.user.lang = 'en';
                }
                //check Registration date
                if ($scope.user.regDate != undefined) {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }
                //check send e-mail
                $scope.email = $scope.user.sendEmail == undefined ? $scope.user.email : $scope.user.sendEmail;
                if (typeof data.techs != 'undefined') {
                    $scope.techs = data.techs;
                }
                if (typeof data.contacts != 'undefined') {
                    $scope.contact = data.contacts;
                }
                if (typeof data.allTechs != 'undefined') {
                    $scope.allTechs = data.allTechs;
                    for (var i = 0; i < $scope.techs.length; i++) {
                        for (var j = 0; j < $scope.allTechs.length; j++) {
                            if ($scope.techs[i].id == $scope.allTechs[j].id)
                                $scope.allTechs[j].ticked = true;
                        }
                    }
                }
                //check for empty image
                if ($scope.user.imgUrl == null) {
                    $scope.img = 'images/profile/no-image.png';
                } else {
                    $scope.img = $scope.user.imgUrl + 'md.jpg?id=' + random;
                    $rootScope.globals.currentUser.img = $scope.user.imgUrl + 'sm.jpg?id=' + random;
                }
                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);
            });
        };

        if ($rootScope.globals.currentUser.role == 'developer') {
            $scope.getDevPersonal();
        }

        $scope.getCustPersonal = function () {
            personalAPI.getCustPersonal().success(function (data) {
                var random = Math.random();
                $scope.user = data.cust;

                if ($scope.user.lang == undefined) {
                    $scope.user.lang = 'en';
                }
                //check Registration date
                if ($scope.user.regDate != undefined) {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }
                //check send e-mail
                $scope.email = $scope.user.sendEmail == undefined ? $scope.user.email : $scope.user.sendEmail;

                //check for empty Json
                if (typeof data.contacts != 'undefined') {
                    $scope.contact = data.contacts;
                }
                //check for empty image
                if ($scope.user.imgUrl == null) {
                    $scope.img = 'images/profile/no-image.png';
                } else {
                    $scope.img = $scope.user.imgUrl + 'md.jpg?id=' + random;
                    $rootScope.globals.currentUser.img = $scope.user.imgUrl + 'sm.jpg?id=' + random;
                }
                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);
            });
        };

        if ($rootScope.globals.currentUser.role == 'customer') {
            $scope.getCustPersonal();
        }

        $scope.getAdminPersonal = function () {
            personalAPI.getAdminPersonal().success(function (data) {
                var random = Math.random();
                $scope.user = data;

                if ($scope.user.lang == undefined) {
                    $scope.user.lang = 'en';
                }
                //check Registration date
                if ($scope.user.regDate != undefined) {
                    $scope.user.regDate = new Date($scope.user.regDate).getTime();
                }
                //check send e-mail
                $scope.email = $scope.user.sendEmail == undefined ? $scope.user.email : $scope.user.sendEmail;
                //check for empty image

                if ($scope.user.imgUrl == null) {
                    $scope.img = 'images/profile/no-image.png';
                } else {
                    $scope.img = $scope.user.imgUrl + 'md.jpg?id=' + random;
                    $rootScope.globals.currentUser.img = $scope.user.imgUrl + 'sm.jpg?id=' + random;
                }
                adminTemp = clone($scope.user);
            });
        };

        if ($rootScope.globals.currentUser.role == 'admin') {
            $scope.getAdminPersonal();
        }

        $scope.save = function () {
            var devJson, techsJson, contactJson;
            $scope.editClass = 'editClass';
            $scope.hide = false;
            if ($rootScope.globals.currentUser.role == 'developer') {
                var selectedTechs = []; //techs ids for sending on backend
                $scope.techs = []; //dev techs
                for (var i = 0; i < $scope.allTechs.length; i++) {
                    if ($scope.allTechs[i].ticked) {
                        selectedTechs.push($scope.allTechs[i].id);
                        $scope.techs.push($scope.allTechs[i]);
                    }
                }
                devJson = angular.toJson($scope.user);
                techsJson = angular.toJson(selectedTechs);
                contactJson = angular.toJson($scope.contact);

                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);

                var technology = [];
                angular.forEach($scope.selectedTechs, function (value, key) {
                    technology.push(value.id);
                });

                personalAPI.sendDevData(devJson, techsJson, contactJson).success(function (data) {
                    $scope.result = data;
                    Notification.success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('personal.all-data-was-up')
                    });
                }).error(function () {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.smth-wrong-sav-data')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'customer') {
                var custJson;
                custJson = angular.toJson($scope.user);
                contactJson = angular.toJson($scope.contact);

                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);

                personalAPI.sendCustData(custJson, contactJson).success(function (data) {
                    $scope.result = data;
                    Notification.success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('personal.all-data-was-up')
                    });
                }).error(function () {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.smth-wrong-sav-data')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'admin') {
                var adminJson;
                adminJson = angular.toJson($scope.user);
                adminTemp = clone($scope.user);
                $scope.user = adminTemp;

                personalAPI.sendAdminData(adminJson).success(function (data) {
                    $scope.result = data;
                    Notification.success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('personal.all-data-was-up')
                    });
                }).error(function () {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.smth-wrong-sav-data')
                    });
                });
            }
        };

        $scope.hide = false;
        $scope.editClass = 'editClass';

        $scope.enableEditor = function () {
            $scope.editClass = '';
            $scope.hide = true;

            if ($rootScope.globals.currentUser.role === 'developer') {
                devTemp = clone($scope.user);
                techsTemp = clone($scope.techs);
                contTemp = clone($scope.contact);
            }
            if ($rootScope.globals.currentUser.role === 'customer') {
                custTemp = clone($scope.user);
                contTemp = clone($scope.contact);
            }
            if ($rootScope.globals.currentUser.role === 'admin') {
                adminTemp = clone($scope.user);
            }
        };

        $scope.disableEditor = function () {
            $scope.hide = false;
            if ($rootScope.globals.currentUser.role == 'developer') {
                $scope.user = devTemp;
                $scope.techs = techsTemp;
                $scope.contact = contTemp;
            }
            if ($rootScope.globals.currentUser.role == 'customer') {
                $scope.user = custTemp;
                $scope.contact = contTemp;
            }
            if ($rootScope.globals.currentUser.role == 'admin') {
                $scope.user = adminTemp;
            }
            $scope.editClass = 'editClass';
        };

        function clone(obj) {
            if (obj === null || typeof(obj) !== 'object' || 'isActiveClone' in obj)
                return obj;
            var temp = obj.constructor(); // changed
            for (var key in obj) {
                if (Object.prototype.hasOwnProperty.call(obj, key)) {
                    obj['isActiveClone'] = null;
                    temp[key] = clone(obj[key]);
                    delete obj['isActiveClone'];
                }
            }
            return temp;
        }

        $scope.resetPassword = function () {
            $scope.newPassword = '';
            $scope.confirmPassword = '';
            $scope.password = '';
        };

        $scope.resetEmail = function () {
            $scope.newEmail = '';
        };

        $scope.changePassword = function () {
            $scope.confirmCode = '';
            if ($rootScope.globals.currentUser.role == 'developer') {
                personalAPI.changeDevPassword($scope.password).success(function (data) {
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPasswordTemp = clone($scope.newPassword);
                    $scope.showTabDialog();
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-old-pass')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'customer') {
                personalAPI.changeCustPassword($scope.password).success(function (data) {
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPasswordTemp = clone($scope.newPassword);
                    $scope.showTabDialog();
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-old-pass')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'admin') {
                personalAPI.changeAdminPassword($scope.password).success(function (data) {
                    $scope.result = data;
                    $scope.confirmPasswordFlag = true;
                    $scope.confirmPasswordTemp = $scope.newPassword;
                    $scope.showTabDialog();

                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-old-pass')
                    });
                });
            }
        };

        $scope.changePasswordAfterConfirm = function () {
            if ($rootScope.globals.currentUser.role == 'developer') {
                personalAPI.confirmCodeAndChangeDevPasswordOrEmail($scope.confirmPasswordTemp, null, $scope.confirmCode).success(function (data) {
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    $mdDialog.cancel();
                    Notification.success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('personal.success-password-changing')
                    });
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-confirm-code')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'customer') {
                personalAPI.confirmCodeAndChangeCustPasswordOrEmail($scope.confirmPasswordTemp, null, $scope.confirmCode).success(function (data) {
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    $mdDialog.cancel();
                    Notification.success({
                        title: $translate.instant('notification.success'),
                        message: $translate.instant('personal.success-password-changing')
                    });
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-confirm-code')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'admin') {
                personalAPI.confirmCodeAndChangeAdminPasswordOrEmail($scope.confirmPasswordTemp, null, $scope.confirmCode).success(function (data) {
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    $scope.showDialogConfirm();
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-confirm-code')
                    });
                });
            }
        };
        $scope.changeSendingEmail = function () {
            $scope.confirmCode = '';
            if ($rootScope.globals.currentUser.role == 'developer') {
                personalAPI.changeDevSendingEmail($scope.newEmail).success(function (data) {
                    $scope.confirmEmailFlag = true;
                    $scope.newEmailTemp = clone($scope.newEmail);
                    $scope.showEmailDialog();
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.email-in-using')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'customer') {
                personalAPI.changeCustSendingEmail($scope.newEmail).success(function (data) {
                    $scope.confirmEmailFlag = true;
                    $scope.newEmailTemp = clone($scope.newEmail);
                    $scope.showEmailDialog();
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'admin') {
                personalAPI.changeAdminSendingEmail($scope.newEmail).success(function (data) {
                    $scope.result = data;
                    $scope.confirmEmailFlag = true;
                    $scope.newEmailTemp = clone($scope.newEmail);
                    $scope.showTabDialog();

                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('notification.smth-wrong')
                    });
                });
            }
        };

        $scope.changeSendingEmailAfterConfirm = function () {
            console.log("method changeSendingEmailAfterConfirm()");
            if ($rootScope.globals.currentUser.role == 'developer') {
                personalAPI.confirmCodeAndChangeDevPasswordOrEmail(null, $scope.newEmailTemp, $scope.confirmCode).success(function (data) {
                    $scope.changePswdOrEmailForUser(data);
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-confirm-code')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'customer') {
                personalAPI.confirmCodeAndChangeCustPasswordOrEmail(null, $scope.newEmailTemp, $scope.confirmCode).success(function (data) {
                    $scope.changePswdOrEmailForUser(data);
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-confirm-code')
                    });
                });
            }
            if ($rootScope.globals.currentUser.role == 'admin') {
                personalAPI.confirmCodeAndChangeAdminPasswordOrEmail(null, $scope.newEmailTemp, $scope.confirmCode).success(function (data) {
                    $scope.result = data;
                    $scope.flagConfirmPhoneCode = true;
                    $scope.showDialogConfirm();
                }).error(function (response) {
                    Notification.error({
                        title: $translate.instant('notification.error'),
                        message: $translate.instant('personal.wrong-confirm-code')
                    });
                });
            }
        };

        $scope.changePswdOrEmailForUser = function (data) {
            $scope.result = data;
            $scope.flagConfirmPhoneCode = true;
            $mdDialog.cancel();
            $scope.email = $scope.newEmailTemp;
            Notification.success({
                title: $translate.instant('notification.success'),
                message: $translate.instant('personal.success-email-changing')
            });
        };

        $scope.showTabDialog = function ($event) {
            $mdDialog.show({
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

        $scope.showEmailDialog = function ($event) {
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

        $scope.confirmEmailDialog = function ($event) {
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

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.showDialogConfirm = function ($event) {
            $mdDialog.show({
                //parent: angular.element(document.body),
                targetEvent: $event,
                scope: $scope,
                preserveScope: true,
                templateUrl: 'app/components/personal/passwordChanged.html',
                controller: 'personalCtrl',
                onComplete: afterShowAnimation,
                locals: {flagConfirmPhoneCode: $scope.flagConfirmPhoneCode}
            });
            function afterShowAnimation(scope, element, options) {

            }
        };

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        $scope.close = function () {
            $mdDialog.hide();
        };

        $scope.uploadFromDevice = function (e, callback) {
            var img = new Image();
            var canvas = document.createElement("canvas");
            var file = document.getElementById('file').files[0];
            if (file.type == 'image/jpeg' || file.type == 'image/gif' || file.type == 'image/png') {
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

                    if ($rootScope.globals.currentUser.role == 'developer') {
                        personalAPI.sendDevImage(base).success(function (data) {
                            $scope.result = data;
                            $scope.getDevPersonal();
                            Notification.success({
                                title: $translate.instant('notification.success'),
                                message: $translate.instant('personal.suc-upload-img')
                            });
                            $rootScope.showProfile = false;
                        }).error(function () {
                            Notification.error({
                                title: $translate.instant('notification.error'),
                                message: $translate.instant('personal.wrong-upload-img')
                            });
                        });
                    }
                    if ($rootScope.globals.currentUser.role == 'customer') {
                        personalAPI.sendCustImage(base).success(function (data) {
                            $scope.result = data;
                            $scope.getCustPersonal();
                            Notification.success({
                                title: $translate.instant('notification.success'),
                                message: $translate.instant('personal.suc-upload-img')
                            });
                            $rootScope.showProfile = false;
                        }).error(function () {
                            Notification.error({
                                title: $translate.instant('notification.error'),
                                message: $translate.instant('personal.wrong-upload-img')
                            });
                        });
                    }
                    if ($rootScope.globals.currentUser.role == 'admin') {
                        personalAPI.sendAdminImage(base).success(function (data) {
                            $scope.result = data;
                            $scope.getAdminPersonal();
                            Notification.success({
                                title: $translate.instant('notification.success'),
                                message: $translate.instant('personal.suc-upload-img')
                            });
                            $rootScope.showProfile = false;
                        }).error(function () {
                            Notification.error({
                                title: $translate.instant('notification.error'),
                                message: $translate.instant('personal.wrong-upload-img')
                            });
                        });
                    }
                }
            } else {
                Notification.error({
                    title: $translate.instant('notification.error'),
                    message: $translate.instant('personal.jpg-png-gif')
                });
            }
        };

        $scope.loadTechs = function ($scope, $http, $query) {
            return $http.post("/user/orders/tech").success(function (data) {
                var techs = data;
                return techs.filter(function (tech) {
                    return tech.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
                });
            });
        };

        $scope.timeZones = [
            {
                zone: "-12",
                title: "-12 Baker island, Howland island",
                ticked: false
            },
            {
                zone: "-11",
                title: "-11 American Samoa, Niue",
                ticked: false
            },
            {
                zone: "-10",
                title: "-10 Hawaii",
                ticked: false
            },
            {
                zone: "-9",
                title: "-9 Marquesas Islands, Gamblie Islands",
                ticked: false
            },
            {
                zone: "-8",
                title: "-8 British Columbia, Mexico, California",
                ticked: false
            },
            {
                zone: "-7",
                title: "-7 British Columbia, US Arizona",
                ticked: false
            },
            {
                zone: "-6",
                title: "-6 Canada Saskatchewan, Costa Rica, Guatemala, Honduras",
                ticked: false
            },
            {
                zone: "-5",
                title: "-5 Colombia, Cuba, Ecuador, Peru",
                ticked: false
            },
            {
                zone: "-4",
                title: "-4 Venezuela, Bolivia, Brazil,	Barbados",
                ticked: false
            },
            {
                zone: "-3",
                title: "-3 Newfoundland, Argentina, Chile",
                ticked: false
            },
            {
                zone: "-2",
                title: "-2 South Georgia",
                ticked: false
            },
            {
                zone: "-1",
                title: "-1 Capa Verde",
                ticked: false
            },
            {
                zone: "0",
                title: "0 Ghana, Iceland, Senegal",
                ticked: false
            },
            {
                zone: "1",
                title: "+1 Algeria, Nigeria, Tunisia",
                ticked: false
            },
            {
                zone: "2",
                title: "+2 Ukraine, Zambia, Egypt",
                ticked: false
            },
            {
                zone: "3",
                title: "+3 Belarus, Iraq, Iran",
                ticked: false
            },
            {
                zone: "4",
                title: "+4 Armenia, Georgia, Oman",
                ticked: false
            },
            {
                zone: "5",
                title: "+5 Kazakhstan, Pakistan, India",
                ticked: false
            },
            {
                zone: "6",
                title: "+6 Ural, Bangladesh",
                ticked: false
            },
            {
                zone: "7",
                title: "+7 Western Indonesai, Thailand",
                ticked: false
            },
            {
                zone: "8",
                title: "+8 Hong Kong, China, Taiwan, Australia",
                ticked: false
            }, {
                zone: "9",
                title: "+9 Timor,Japan",
                ticked: false
            }, {
                zone: "10",
                title: "+10 New Guinea, Australia",
                ticked: false
            }, {
                zone: "11",
                title: "+11 Solomon Islands, Vanuatu",
                ticked: false
            }, {
                zone: "12",
                title: "+12 New zealand, Kamchatka, Kiribati",
                ticked: false
            }];
    }]);
