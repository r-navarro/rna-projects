'use strict';
angular.module('demo-hockey', [ 'ngRoute', 'ngAnimate','ngResource', 'ui.bootstrap', 'ngCookies' ]).config(
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
			}).when('/show/:vehicleId', {
                templateUrl : 'partials/vehicle/show.html',
                controller : 'VehicleBoardController'
            }).when('/:vehicleId/ft/create', {
				templateUrl : 'partials/fullTank/create.html',
				controller : 'FullTankController'
		  	}).when('/login', {
				templateUrl : 'partials/login.html',
				controller : 'LoginController'
			}).otherwise({
				redirectTo : '/login'
			});
			
		} ])
		.run(['$rootScope', '$location', '$cookieStore', '$http',
            function ($rootScope, $location, $cookieStore, $http) {
                // keep user logged in after page refresh
                $rootScope.globals = $cookieStore.get('globals') || {};
                if ($rootScope.globals.currentUser) {
                    $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
                }

                $rootScope.$on('$locationChangeStart', function (event, next, current) {
                    // redirect to login page if not logged in
                    if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
                        $location.path('/login');
                    }
                });
            }]);
