angular.module('carManagement.user', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/user', {
            templateUrl: 'components/user/show.html',
            controller: 'UserController'
        })
        .when('/user/update', {
            templateUrl: 'components/user/update.html',
            controller: 'UserController'
        }).when('/user/create', {
            templateUrl: 'components/user/create.html',
            controller: 'UserController'
        });
}])

.controller('UserController', ['$scope', 'UserService', function($scope, UserService) {

    $scope.user = {};

    $scope.get = function() {
        UserService.get().then(function(data) {
            $scope.user = data;
        });
    };

    $scope.update = function() {
        UserService.update($scope.user.id, $scope.user).then(function(data) {
            $scope.user = data;
        });
    };

    $scope.create = function() {
        UserService.create($scope.user).then(function(data) {
            $scope.user = data;
        });
    };

}]);
