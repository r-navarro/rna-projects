angular.module('carManagement.login', ['ngRoute'])

.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/home', {
        templateUrl : 'components/login/home.html',
        controller : 'HomeController'
    }).when('/login', {
        templateUrl : 'components/login/login.html',
        controller : 'LoginController'
    });
}])

.controller('LoginController',
    function($scope, $rootScope, $location, AuthenticationService) {

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

        $scope.getCurrentUser = function() {
            if($rootScope.globals.currentUser) {
                return $rootScope.globals.currentUser.username;
            }

            return '';
        }
    }
);