angular.module('carManagement')
	.factory('AuthenticationService', ['$resource', 'DaoService','Base64', '$http', '$cookieStore', '$rootScope',
    function($resource, DaoService, Base64, $http, $cookieStore, $rootScope) {
    	var service = {};

        service.authenticate = function(credentials, callback) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password)
            } : {};

            $http.get('login', {headers : headers}).success(function(data) {
              if (data.name) {
                $rootScope.authenticated = true;
                $rootScope.username = data.name;
              } else {
                $rootScope.authenticated = false;
              }
              callback && callback();
            }).error(function() {
              $rootScope.authenticated = false;
              callback && callback();
            });

        };

        service.logout = function() {
			$http.post('logout', {}).success(function() {
				$rootScope.authenticated = false;
			}).error(function(data) {
				$rootScope.authenticated = false;
			});
        }

		return service;
	}
]);