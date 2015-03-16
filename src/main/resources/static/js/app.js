'use strict';
angular.module('demo-hockey', [ 'ngRoute', 'ngAnimate','ngResource', 'ui.bootstrap' ]).config(
		[ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
			$routeProvider.when('/home', {
				templateUrl : 'partials/home.html',
				controller : 'HomeController'
			}).when('/create', {
				templateUrl : 'partials/vehicle/create.html',
				controller : 'VehicleController'
			}).when('/list', {
				templateUrl : 'partials/vehicle/list.html',
				controller : 'VehicleController'
			}).otherwise({
				redirectTo : '/home'
			});
			
		} ]);
