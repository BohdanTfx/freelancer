angular.module('FreelancerApp')
    .controller('homeCtrl', function ($scope,$translate, $location, $timeout) {
        $scope.myInterval = 4000;
        $scope.noWrapSlides = false;
        var slides = $scope.slides = [];

        $timeout(function(){$location.search('asgfsdg', 'sdg');
            $location.search('name', 'dgdffdg')}, 5000);

        $scope.slider = {};
        $scope.slider.image1 = $translate.instant("home.slider-image-1");
        $scope.slider.image2 = $translate.instant("home.slider-image-2");
        $scope.slider.image3 = $translate.instant("home.slider-image-3");
        $scope.slider.image4 = $translate.instant("home.slider-image-4");

        $scope.addSlide = function () {
            slides.push({
                image: 'stylesheet" href="../../../images/home/Fotolia_74087814_Subscription_Monthly_M.jpg',
                text:  $scope.slider.image1
            });
            slides.push({
                image: 'stylesheet" href="../../../images/home/../../../images/home/freelance-work-ftr.jpg',
                text:  $scope.slider.image2
            });

            slides.push({
                image: 'stylesheet" href="../../../images/home/test.jpg',
                text:  $scope.slider.image3
            });
            slides.push({
                image: 'stylesheet" href="../../../images/home/cust_home_bg.jpg',
                text:  $scope.slider.image4
            });
        };

        $scope.addSlide();

    });