'use strict';

angular.module('FreelancerApp')
    .factory('personalAPI', ['$http','config', function ($http, config) {
        var dataFactory = {};

        dataFactory.getDevPersonal = function() {
            return $http.get('/dev/getpersonaldevdata');
        };

        dataFactory.sendDevData = function (developer, technologies, contact) {
            var data = $.param({
                developer: developer,
                technologies: technologies,
                contact: contact
            });
            return $http({
                method: 'POST',
                url: '/dev/sendpersonaldata',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        dataFactory.sendDevData = function (customer, contact) {
            var data = $.param({
                customer: customer,
                contact: contact
            });
            return $http({
                method: 'POST',
                url: '/cust/sendpersonaldata',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        dataFactory.sendImage = function(image) {
            var data = $.param({
                image: image
            });
            return $http({
                method: 'POST',
                url: '/dev/uploadImage',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            })
        };

        dataFactory.changeDevPassword = function (password, newPassword) {

            var data = 'password=' + password + '&newPassword=' + newPassword;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/dev/changedevpassword', data);
        };


        dataFactory.changeCustPassword = function (password, newPassword) {

            var data = 'password=' + password + '&newPassword=' + newPassword;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/cust/changecustpassword', data);
        };

        dataFactory.confirmCodeAndChangeDevPassword = function (confirmPassword) {

            var data = 'password=' + confirmPassword;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/dev/confirmchangepassword', data);
        };

        dataFactory.confirmCodeAndChangeCustPassword = function (confirmPassword) {

            var data = 'password=' + confirmPassword;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/cust/confirmchangepassword', data);
        };

        //dataFactory.upload = function(files){
        //    var data = $.param({
        //        image: files
        //    });
        //    return $http({
        //        method: 'POST',
        //        url: '/dev/uploadImage',
        //        headers: {'Content-Type' : 'multipart/form-data'},
        //        data: data
        //    })
        //};

        dataFactory.getCustPersonal = function () {
            return $http.get('/cust/getpersonalcustdata');
        };

        dataFactory.sendCustData = function (customer, contact) {
            var data = $.param({
                customer: customer,
                contact: contact
            });
            return $http({
                method: 'POST',
                url: '/cust/sendpersonaldata',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        return dataFactory;
    }]);
