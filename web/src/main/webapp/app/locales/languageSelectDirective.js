angular.module('FreelancerApp') .directive('ngTranslateLanguageSelect', function (localeService) { 'use strict';

    return {
        restrict: 'A',
        replace: true,
        template: '' +
        '<span class="language-select" ng-if="visible">'+
        '<md-input-container class="language-select">'+
        '<md-select ng-model="currentLocaleDisplayName" placeholder="{{\'directives.language-select.Language\' | translate}}"' +
        'class="inner-elements-color-blue-light" >'+
        '<md-option ng-click="changeLanguage(localesDisplayNames[0])">{{localesDisplayNames[0]}}</md-option>'+
        '<md-option ng-click="changeLanguage(localesDisplayNames[1])">{{localesDisplayNames[1]}}</md-option>'+
        '</md-select>'+
        '</md-input-container>'+
        '</span>'+
        '',
       //         '<md-input-container class="language-select">'+
       //      '<md-select ng-model="currentLocaleDisplayName" placeholder="{{\"directives.language-select.Language\" | translate}}">'+
       //          '<md-option ng-click="changeLanguage(localesDisplayNames[0])">{{localesDisplayNames[0]}}</md-option>'+
       //           '<md-option ng-click="changeLanguage(localesDisplayNames[1])">{{localesDisplayNames[1]}}</md-option>'+
       //      '</md-select>'+
       //'</md-input-container>',








        controller: function ($scope) {
            $scope.currentLocaleDisplayName = localeService.getLocaleDisplayName();
            $scope.localesDisplayNames = localeService.getLocalesDisplayNames();
            $scope.visible = $scope.localesDisplayNames &&
            $scope.localesDisplayNames.length > 1;

            $scope.changeLanguage = function (locale) {
                localeService.setLocaleByDisplayName(locale);
            };
        }
    };
});/**
 * Created by Rynik on 08.02.2016.
 */






//
//template: ''+
//'<div class="language-select" ng-if="visible">'+
//'<label>'+
//'{{"directives.language-select.Language" | translate}}:'+
//'<select ng-model="currentLocaleDisplayName"'+
//'ng-options="localesDisplayName for localesDisplayName in localesDisplayNames"'+
//'ng-change="changeLanguage(currentLocaleDisplayName)">'+
//'</select>'+
//'</label>'+
//'</div>'+
//''