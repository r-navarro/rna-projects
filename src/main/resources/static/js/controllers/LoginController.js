angular.module('demo-hockey').controller('LoginController',
		function($scope, $rootScope, $location, AuthenticationService) {

			// reset login status
            AuthenticationService.clearCredentials();

            $scope.login = function () {
                $scope.dataLoading = true;
                AuthenticationService.setCredentials($scope.username, $scope.password);
                AuthenticationService.login($scope.username, $scope.password, function(response) {
                    if(response.status == 200) {
                        $location.path('/list');
                    } else {
                        AuthenticationService.clearCredentials();
                        $scope.error = "Error : " + response.data.message;
                        $scope.dataLoading = false;
                    }
                });
            };

            $scope.logout = function(){
                AuthenticationService.clearCredentials();
                $location.path('/');
            };
		}
);