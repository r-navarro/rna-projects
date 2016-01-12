angular.module('carManagement.login', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/home', {
        templateUrl: 'components/login/home.html',
        controller: 'HomeController'
    }).when('/login', {
        templateUrl: 'components/login/login.html',
        controller: 'LoginController',
        resolve: {
            'checkLoging': ["AuthenticationService", function(AuthenticationService) {
                AuthenticationService.authenticate();
            }]
        }
    });
}])

.controller('LoginController', ['$scope', '$rootScope', '$location', '$http', 'AuthenticationService',
    function($scope, $rootScope, $location, $http, AuthenticationService) {

        $scope.login = function() {
            AuthenticationService.authenticate($scope.credentials, function() {
                if ($rootScope.authenticated) {
                    $location.path("/list");
                } else {
                    $location.path("/login");
                    $scope.error = 'wrong login or password';
                }
            });
        };


        $scope.logout = function() {
            AuthenticationService.logout(function() {
                $location.path("/login");
            });
        };

        $scope.getCurrentUser = function() {
            if ($rootScope.authenticated) {
                return $rootScope.username;
            }

            return '';
        };

        $scope.isCurrentUserAdmin = function() {
            return $rootScope.isAdmin;
        };

        $scope.goHomePage = function() {
            if ($rootScope.authenticated) {
                $location.path("/list");
            } else {
                $location.path("/login");
            }
        };


    }
]);
