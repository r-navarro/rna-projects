angular.module('demo-hockey').controller('LoginController',
		function($scope, $rootScope, $location, AuthenticationService) {

			// reset login status
            AuthenticationService.clearCredentials();

            $scope.login = function () {
                $scope.dataLoading = true;
                AuthenticationService.login($scope.username, $scope.password, function(response) {
                    if(response.status == 200) {
                        AuthenticationService.setCredentials($scope.username, $scope.password);
                        $location.path('/list');
                    } else {
                        $scope.error = "Error : " + response;
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