'use strict';

/**
 * @ngdoc function
 * @name resourcesApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the resourcesApp
 */
angular.module('resourcesApp')
  .controller('MainCtrl', function ($scope,$resource) {
	 $scope.saying= $resource('/api/hello-world').get();
  });
