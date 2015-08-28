'use strict';
angular.module('carManagement', [ 'ngRoute', 'ngAnimate','ngResource', 'ui.bootstrap', 'ngCookies', 'nvd3ChartDirectives',
	'carManagement.vehicle',
	'carManagement.fullTank',
	'carManagement.login' ])
.config(
	[ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
		$routeProvider.otherwise({
			redirectTo : '/login'
		});
		
	} ])

.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authData; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                $location.path('/login');
            }
    });
}]);
