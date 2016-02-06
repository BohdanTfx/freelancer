angular.module('FreelancerApp')
    .controller('homeCtrl', function ($scope) {
        $scope.myInterval = 4000;
        $scope.noWrapSlides = false;
        var slides = $scope.slides = [];

        $scope.addSlide = function () {
            slides.push({
                image: 'stylesheet" href="../../../images/home/Fotolia_74087814_Subscription_Monthly_M.jpg',
                text: 'Find & Hire Talented Freelancers'
            });
            slides.push({
                image: 'stylesheet" href="../../../images/home/../../../images/home/freelance-work-ftr.jpg',
                text: 'Work at office, home or wherever you want'
            });

            slides.push({
                image: 'stylesheet" href="../../../images/home/test.jpg',
                text: 'Develop yourself with our tests'
            });
            slides.push({
                image: 'stylesheet" href="../../../images/home/cust_home_bg.jpg',
                text: 'Work hard - earn more'
            });
        };

        $scope.addSlide();

    });