angular.module('demo-hockey')
	.factory('AuthenticationService', ['$resource', 'DaoService','Base64', '$http', '$cookieStore', '$rootScope'
    function($resource, DaoService, Base64, $http, $cookieStore, $rootScope) {
    	var service = {};

         service.Login = function (username, password, callback) {
         	DaoService
         };

         service.SetCredentials = function (username, password) {
			 var authdata = Base64.encode(username + ':' + password);

			 $rootScope.globals = {
				 currentUser: {
					 username: username,
					 authdata: authdata
				 }
			 };

			 $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
			 $cookieStore.put('globals', $rootScope.globals);
		 };

		 service.ClearCredentials = function () {
			 $rootScope.globals = {};
			 $cookieStore.remove('globals');
			 $http.defaults.headers.common.Authorization = 'Basic ';
		 };

		 return service;
	}
]);