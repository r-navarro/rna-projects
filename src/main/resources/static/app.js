'use strict';
angular.module('carManagement', [ 'ngRoute', 'ngAnimate','ngResource', 'ui.bootstrap', 'ngCookies', 'nvd3ChartDirectives',
	'carManagement.vehicle',
	'carManagement.fullTank',
	'carManagement.login' ])
.config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
		$routeProvider.otherwise({
			redirectTo : '/login'
		});
		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	} 
]);

