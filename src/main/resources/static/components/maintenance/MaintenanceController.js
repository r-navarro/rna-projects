angular.module('carManagement.maintenance', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/:vehicleId/maintenance/create', {
        templateUrl: 'components/maintenance/create.html',
        controller: 'MaintenanceController'
    }).when('/:vehicleId/maintenance/update/:mtId', {
        templateUrl: 'components/maintenance/update.html',
        controller: 'MaintenanceController'
    });
}])

.controller('MaintenanceController', ["$scope", "MaintenanceService", "VehicleService", "$routeParams", "$location", function($scope, MaintenanceService, VehicleService, $routeParams, $location) {

    $scope.maintenance = {};

    $scope.vehicleId = $routeParams.vehicleId;

    $scope.mtId = $routeParams.mtId;

    $scope.create = function(maintenance) {
        MaintenanceService.save($scope.vehicleId, maintenance).then(function() {
            $location.path('/show/' + $scope.vehicleId);
        });
    };

    $scope.update = function(maintenance) {
        MaintenanceService.update($scope.vehicleId, maintenance).then(function() {
            $location.path('/show/' + $scope.vehicleId);
        });
    };

    $scope.init = function() {
        if ($scope.mtId) {
            MaintenanceService.get($scope.vehicleId, $scope.mtId).then(function(mt) {
                $scope.maintenance = mt;
                $scope.maintenance.date = new Date(mt.date);
                $scope.maintenance.predictedDate = new Date(mt.predictedDate);
            });
        } else {
            $scope.maintenance.date = new Date();
        }
    };

    $scope.init();

    $scope.open = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.opened = true;
        $scope.openedPredicted = false;
    };

    $scope.openPredicted = function($event) {
        $event.preventDefault();
        $event.stopPropagation();

        $scope.openedPredicted = true;
        $scope.opened = false;
    };

    $scope.format = "dd/MM/yyyy";
}]);
