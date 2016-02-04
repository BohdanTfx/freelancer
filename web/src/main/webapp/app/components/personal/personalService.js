'use strict';

angular.module('FreelancerApp')
    .factory('personalAPI', function ($http, config) {
        var urlBase = '/dev/personal',
            dataFactory = {};

        dataFactory.getPersonal = function () {
            return $http.get(urlBase);
        };

        var urlResult = '/dev/sendpersonaldata';

        dataFactory.sendData = function (developer, technologies, contact) {
            var data = $.param({
                developer: developer,
                technologies: technologies,
                contact: contact
            });
            return $http({
                method: 'POST',
                url: urlResult,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        var urlImage = '/dev/uploadImage';

        dataFactory.sendImage = function (image) {
            var data = $.param({
                image: image
            });
            return $http({
                method: 'POST',
                url: urlImage,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            })
        };
        dataFactory.upload = function (files) {
            var data = $.param({
                image: files
            });
            return $http({
                method: 'POST',
                url: urlImage,
                headers: {'Content-Type': 'multipart/form-data'},
                data: data
            })


        };

        return dataFactory;
    });
