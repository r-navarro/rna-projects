angular.module('carManagement.vehicle')
    .controller('VehicleController', ["$scope", "VehicleService", "$routeParams", "$location", function($scope, VehicleService, $routeParams, $location) {

        $scope.totalPages = 0;
        $scope.currentPage = 1;
        $scope.totalElements = 0;
        $scope.itemsPerPage = 0;
        $scope.vehicleId = $routeParams.vehicleId;


        $scope.updateList = function(page) {
            VehicleService.list(page).then(function(list) {
                $scope.vehicles = list.content;
                $scope.totalPages = list.totalPages;
                $scope.totalElements = list.totalElements;
                $scope.itemsPerPage = list.size;
            });
        };

        $scope.pageChanged = function() {
            $scope.updateList($scope.currentPage);
        };

        $scope.getVehicle = function() {
            VehicleService.get($scope.vehicleId).then(function(vehicle) {
                $scope.vehicle = vehicle;
            });
        };

        $scope.create = function(vehicle) {
            VehicleService.save(vehicle, callback, errorCallback);
        };

        $scope.deleteVehicle = function(id) {
            VehicleService.remove(id).then(function(data) {
                $scope.updateList(1);
            });
        };

        $scope.update = function(vehicle) {
            VehicleService.update(vehicle, callback, errorCallback);
        };

        callback = function() {
            $location.path('/list');
        };

        errorCallback = function(message) {
            $scope.error = message;
        };
    }]);
