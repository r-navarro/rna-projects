'use strict';
angular.module('carManagement', [ 'ngRoute', 'ngAnimate','ngResource', 'ui.bootstrap', 'ngCookies', 'nvd3ChartDirectives',
	'carManagement.vehicle',
	'carManagement.fullTank',
	'carManagement.maintenance',
	'carManagement.login',
	'carManagement.user' ])
.config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
		$routeProvider.otherwise({
			redirectTo : '/login'
		});
		$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	} 
])
.run(['$rootScope', '$location', 'AuthenticationService', function($rootScope, $location, AuthenticationService){	
    $rootScope.$on('$routeChangeStart', function (event) {
        AuthenticationService.authenticate();
        if($rootScope.authenticated == false) {
        	$location.path("/login");
    	}
    });
}]);

