angular.module('demo-hockey')
	.factory('AuthenticationService', ['$resource', 'DaoService','Base64', '$http', '$cookieStore', '$rootScope',
    function($resource, DaoService, Base64, $http, $cookieStore, $rootScope) {
    	var service = {};

         service.login = function (username, password, callback) {
            user = {
                username:username,
                password:password
            }
         	DaoService.getData("/login", 'POST', user).then(function(response){
                callback(response);
            });
         };

         service.setCredentials = function (username, password) {
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

		 service.clearCredentials = function () {
			 $rootScope.globals = {};
			 $cookieStore.remove('globals');
			 $http.defaults.headers.common.Authorization = 'Basic ';
		 };

		 return service;
	}
]);