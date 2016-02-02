angular.module('FreelancerApp')
    .controller('personalCtrl', function ($scope, $http, personalAPI, $log, $stateParams, $rootScope) {

        var devTemp, techsTemp, contTemp;

        console.log('START PERSONAL');
        console.log($rootScope.role);
        //$scope.devId = $stateParams.id;
        //console.log($scope.devId + '  devID');


        personalAPI.getPersonal().success(function (data) {

            $log.log(data);

            $scope.patternPhone = '[0-9]{11}';

            $scope.dev = data.dev;
            $scope.dev.regDate = new Date($scope.dev.regDate).getTime();

            if (typeof data.techs != 'undefined') {
                $scope.techs = data.techs;
                console.log($scope.techs);
            } else {
                console.log('No technologies');
            }

            if (typeof data.contacts != 'undefined') {
                $scope.cont = data.contacts;
                console.log($scope.cont);
            } else {
                console.log('No contacts');
            }

            $scope.allTechs = data.allTechs;

            devTemp = clone($scope.dev);
            techsTemp = clone($scope.techs);
            contTemp = clone($scope.cont);

            if (typeof $scope.dev.imgUrl == 'undefined')
                $scope.img = 'images/profile/default_logo.jpg';
            else
                $scope.img = $scope.dev.imgUrl;


            $scope.testTech = [
                {name: "Java"},
                {name: "C"},
                {name: "C++"},
                {name: "C#"},
            ];
            $scope.allTestTech = [
                {name: "Test"},
                {name: "Big"},
                {name: "Speed"},
                {name: "Red"}
            ];

        }).error(function () {
            alert('alert');
        });

        $scope.hide = false;
        $scope.editClass = 'editClass';

        $scope.enableEditor = function () {
            $scope.editClass = '';
            $scope.hide = true;

            devTemp = clone($scope.dev);
            techsTemp = clone($scope.techs);
            contTemp = clone($scope.cont);
        };

        $scope.disableEditor = function () {

            $scope.hide = false;

            $scope.dev = devTemp;
            $scope.techs = techsTemp;
            $scope.cont = contTemp;

            console.log(devTemp);

            $scope.editClass = 'editClass';


        };

        $scope.save = function () {
            var devJson, techsJson, contJson;

            $scope.editClass = 'editClass';
            $scope.hide = false;

            console.log($scope.dev.fname);
            devJson = angular.toJson($scope.dev);
            techsJson = angular.toJson($scope.techs);
            contJson = angular.toJson($scope.cont);

            devTemp = angular.copy($scope.dev);
            techsTemp = angular.copy($scope.techs);
            contTemp = angular.copy($scope.cont);

            console.log(devJson);
            console.log(techsJson);
            console.log(contJson);

            personalAPI.sendData(devJson, techsJson, contJson).success(function (data) {
                $log.log(data);
                $scope.result = data;
            }).error(function () {
                alert('Server is busy');
            });
        };

        //$scope.tags = $scope.techs;
        //$scope.loadTags = function(query){
        //    return $http.get(query);
        //};

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


        //$scope.submit = function() {
        //
        //        $scope.upload($scope.file);
        //
        //};
        //$scope.upload = function (file) {
        //    Upload.upload({
        //        url: 'images/dev/',
        //        data: {file: file, 'username': $scope.username}
        //    }).then(function (resp) {
        //        console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
        //    }, function (resp) {
        //        console.log('Error status: ' + resp.status);
        //    }, function (evt) {
        //        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
        //        console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
        //    });
        //};

        //  var  file = $scope.files;
        //  $scope.upload = function(file){
        //      personalAPI.upload(data).success(function (data) {
        //          $log.log(data);
        //          $scope.result = data;
        //      }).error(function () {
        //          alert('Server is busy');
        //      });
        //  }
        //
        //  $scope.add = function(){
        //      var imageJson, imgData, dataUrl;
        //      var file = document.getElementById('file').files[0];
        //
        //
        //      console.log("File name: " + file.fileName);
        //
        //      var reader = new FileReader(file);
        //
        //      reader.onload = function(event) {
        //          // I usually remove the prefix to only keep data, but it depends on your server
        //          var data = event.target.result.replace("data:" + file.type + ";base64,", '');
        //
        //
        //          //img1 = new Image(reader);
        //          console.log('readGood');
        //          //imageJson = JSON.stringify(getBase64Image(img1))
        //          //imgData = getBase64Image(file)
        //          //dataUrl = canvas.toDataURL();
        //          //console.log(imageJson);
        //          console.log('sending image');
        //          personalAPI.sendImage(data).success(function (data) {
        //              $log.log(data);
        //              $scope.result = data;
        //          }).error(function () {
        //              alert('Server is busy');
        //          });
        //
        //          reader.readAsDataURL(file);
        //          console.log(data);
        //      }
        //
        //}
        //
        //
        //  function getBase64Image(imgElem) {
        //      var canvas = document.createElement("canvas");
        //      canvas.width = imgElem.clientWidth;
        //      canvas.height = imgElem.clientHeight;
        //      var ctx = canvas.getContext("2d");
        //      ctx.drawImage(imgElem, 0, 0);
        //      var dataURL = canvas.toDataURL("image/png");
        //      return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
        //  }
        //
        //
        //  $scope.fileToUpload = null;
        //  $scope.upload = function () {
        //      $http({
        //          method: 'POST',
        //          url: 'api/fileupload',
        //          headers: {
        //              'Content-Type': 'multipart/form-data'
        //          },
        //          data: {
        //              upload: $scope.fileToUpload
        //          },
        //          transformRequest: function (data, headersGetter) {
        //              var formData = new FormData();
        //              angular.forEach(data, function (value, key) {
        //                  formData.append(key, value);
        //              });
        //
        //              var headers = headersGetter();
        //              delete headers['Content-Type'];
        //
        //              return formData;
        //          }
        //      })
        //          .success(function (data, status, headers, config) {
        //              alert(data.FName);
        //          })
        //          .error(function (data, status, headers, config) {
        //              alert(data);
        //          });
        //  };


        //$scope.loadTechs = function($query) {
        //    return $http.get('techs.json', {cache: true}).then(function (response) {
        //        var techs = response.data;
        //        return techs.filter(function (tech) {
        //            return tech.name.toLowerCase().indexOf($query.toLowerCase()) != -1;
        //        });
        //    });
        //};


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



    });