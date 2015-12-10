angular.module('carManagement')
	.factory('AuthenticationService', ['$resource', 'DaoService','Base64', '$http', '$cookieStore', '$rootScope',
    function($resource, DaoService, Base64, $http, $cookieStore, $rootScope) {
    	var service = {};

        service.authenticate = function(credentials, callback) {

            var headers = credentials ? {authorization : "Basic "
                + btoa(credentials.username + ":" + credentials.password)
            } : {};

            $http.get('users/login', {headers : headers}).success(function(data) {
              if (data.name) {
                service.isAdmin();
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

        service.logout = function(callback) {
    			$http.post('logout', {}).success(function() {
    				$rootScope.authenticated = false;
            callback && callback();
    			}).error(function(data) {
    				$rootScope.authenticated = false;
            callback && callback();
    			});
        }

        service.isAdmin = function() {
          $rootScope.isAdmin = false;
          $http.get('users/isAdmin').success(function(data) {
            if(data == 'true'){
              $rootScope.isAdmin = true;
            } else {
              $rootScope.isAdmin = false;
            }
          }).error(function(data) {
            console.log(data);
          });;
        }

		return service;
	}
]);