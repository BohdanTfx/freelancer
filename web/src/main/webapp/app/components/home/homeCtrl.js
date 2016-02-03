angular.module('FreelancerApp')
    .controller('homeCtrl', function ($scope) {
        $scope.myInterval = 4000;
        $scope.noWrapSlides = false;
        var slides = $scope.slides = [];

        $scope.addSlide = function () {
            slides.push({
                image: 'stylesheet" href="../../../images/home/Fotolia_74087814_Subscription_Monthly_M.jpg',
                text: 'Fotolia_74087814_Subscription_Monthly_M'
            });
            slides.push({
                image: 'stylesheet" href="../../../images/home/../../../images/home/freelance-work-ftr.jpg',
                text: 'freelance-work-ftr'
            });

            slides.push({
                image: 'stylesheet" href="../../../images/home/it.jpg',
                text: '.technology.jpg'
            });
            slides.push({
                image: 'stylesheet" href="../../../images/home/cust_home_bg.jpg',
                text: '.technology.jpg'
            });
        };

        $scope.addSlide();

    });