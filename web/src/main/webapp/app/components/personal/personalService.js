'use strict';

angular.module('FreelancerApp')
    .factory('personalAPI', ['$http','config', function ($http, config) {
        var dataFactory = {};

        dataFactory.getDevPersonal = function() {
            return $http.get('/dev/getPersonalData');
        };

        dataFactory.sendDevData = function (developer, technologies, contact) {
            var data = $.param({
                developer: developer,
                technologies: technologies,
                contact: contact
            });
            return $http({
                method: 'POST',
                url: '/dev/sendPersonalData',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        dataFactory.sendDevImage = function(image) {
            var data = $.param({
                image: image
            });
            return $http({
                method: 'POST',
                url: '/user/uploadImage',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            })
        };
        dataFactory.sendCustImage = function(image) {
            var data = $.param({
                image: image
            });
            return $http({
                method: 'POST',
                url: '/user/uploadImage',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            })
        };

        dataFactory.sendAdminImage = function(image) {
            var data = $.param({
                image: image
            });
            return $http({
                method: 'POST',
                url: '/user/uploadImage',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            })
        };


        dataFactory.changeDevPassword = function (password, newPassword) {
            var data = 'password=' + password + '&newPassword=' + newPassword;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/dev/changePassword', data);
        };


        dataFactory.changeCustPassword = function (password, newPassword) {
            var data = 'password=' + password + '&newPassword=' + newPassword;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/cust/changePassword', data);
        };

        dataFactory.confirmCodeAndChangeDevPassword = function (confirmPassword, confirmCode) {
            var data = 'password=' + confirmPassword + '&confirmCode=' + confirmCode;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/dev/confirmChangePasswordAndEmail', data);
        };

        dataFactory.confirmCodeAndChangeCustPassword = function (confirmPassword, confirmCode) {
            var data = 'password=' + confirmPassword + '&confirmCode=' + confirmCode;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/cust/confirmChangePasswordAndEmail', data);
        };

        dataFactory.changeDevSendingEmail = function(newEmail) {
            var data = 'email=' + newEmail;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/dev/changeEmail', data);
        };

        dataFactory.changeCustSendingEmail = function(newEmail) {
            var data = 'email=' + newEmail;
            $http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded; charset=UTF-8';

            return $http.post('/cust/changeEmail', data);
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
            return $http.get('/cust/getPersonalData');
        };

        dataFactory.sendCustData = function (customer, contact) {
            var data = $.param({
                customer: customer,
                contact: contact
            });
            return $http({
                method: 'POST',
                url: '/cust/sendPersonalData',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        dataFactory.getAdminPersonal = function(){
            return $http.get('/admin/getPersonalData');
        };
        dataFactory.sendAdminData = function (admin) {
            var data = $.param({
                admin: admin
            });
            return $http({
                method: 'POST',
                url: '/admin/sendPersonalData',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: data
            });
        };

        return dataFactory;
    }]);
