angular.module('carManagement.vehicle', ['ngRoute'])
    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/create', {
            templateUrl: 'components/vehicle/create.html',
            controller: 'VehicleController'
        }).when('/list', {
            templateUrl: 'components/vehicle/list.html',
            controller: 'VehicleController'
        }).when('/update/:vehicleId', {
            templateUrl: 'components/vehicle/update.html',
            controller: 'VehicleController'
        }).when('/show/:vehicleId', {
            templateUrl: 'components/vehicle/show.html',
            controller: 'VehicleBoardController'
        });
    }]).controller('VehicleBoardController', ['$scope', 'VehicleService', 'FullTankService', 'MaintenanceService', '$routeParams', 'ChartService', '$location',
        function($scope, VehicleService, FullTankService, MaintenanceService, $routeParams, ChartService, $location) {

            $scope.fullTankTotalPages = 0;
            $scope.fullTankCurrentPage = 1;
            $scope.fullTankTotalElements = 0;
            $scope.fullTankItemsPerPage = 0;
            $scope.fullTankSortType = 'date';
            $scope.fullTankSortReverse = false;

            $scope.vehicleId = $routeParams.vehicleId;

            $scope.maintenancesTotalPages = 0;
            $scope.maintenancesCurrentPage = 1;
            $scope.maintenancesTotalElements = 0;
            $scope.maintenancesItemsPerPage = 0;
            $scope.maintenancesSortType = 'predictedDate';
            $scope.maintenancesSortReverse = false;

            $scope.updateFullTanks = function(page) {
                FullTankService.list($scope.vehicleId, page, $scope.fullTankSortType, $scope.fullTankSortReverse).then(function(list) {
                    $scope.fullTanks = list.content;
                    $scope.fullTankTotalPages = list.totalPages;
                    $scope.fullTankTotalElements = list.totalElements;
                    $scope.fullTankItemsPerPage = list.size;
                });
            };

            $scope.deleteFullTank = function(fullTank) {
                FullTankService.remove($scope.vehicleId, fullTank).then(function() {
                    $scope.updateFullTanks(1);
                });
            };

            $scope.fulltankPageChanged = function() {
                $scope.updateFullTanks($scope.fullTankCurrentPage);
            };

            $scope.get = function() {
                VehicleService.get($scope.vehicleId).then(function(vehicle) {
                    $scope.vehicle = vehicle;
                });

                FullTankService.costStats($scope.vehicleId).then(function(data) {
                    $scope.costData = ChartService.drawLineChart("Cost", data);
                });

                FullTankService.distanceStats($scope.vehicleId).then(function(data) {
                    $scope.distanceData = ChartService.drawLineChart("Distance", data);
                });

                FullTankService.averageStats($scope.vehicleId).then(function(data) {
                    $scope.averageStats = data;
                });
            };

            $scope.xAxisTickFormat_Date_Format = function() {
                return function(d) {
                    return d3.time.format("%d/%m/%Y")(new Date(d));
                };
            };

            $scope.updateMaintenances = function(page) {
                MaintenanceService.list($scope.vehicleId, page, $scope.maintenancesSortType, $scope.maintenancesSortReverse).then(function(list) {
                    $scope.maintenances = list.content;
                    $scope.maintenancesTotalPages = list.totalPages;
                    $scope.maintenancesTotalElements = list.totalElements;
                    $scope.maintenancesItemsPerPage = list.size;
                });
            };

            $scope.deleteMaintenance = function(maintenance) {
                MaintenanceService.remove($scope.vehicleId, maintenance).then(function() {
                    $scope.updateMaintenances(1);
                });
            };

            $scope.fullTankPageChanged = function() {
                $scope.updateMaintenances($scope.maintenancesCurrentPage);
            };

            $scope.getDateStyle = function(maintenance) {
                today = new Date();
                if (maintenance.predictedDate < today) {
                    if (maintenance.date <= maintenance.predictedDate) {
                        return "text-danger bg-danger";
                    }
                }
                return "";
            };

            $scope.sortMaintenance = function(header) {
                if (header == $scope.maintenancesSortType) {
                    $scope.maintenancesSortReverse = !$scope.maintenancesSortReverse;
                } else {
                    $scope.maintenancesSortType = header;
                    $scope.maintenancesSortReverse = false;
                }
                $scope.updateMaintenances($scope.maintenancesCurrentPage);
            };

            $scope.sortFullTank = function(header) {
                console.log("controller sort : " + $scope.fullTankCurrentPage);
                if (header == $scope.fullTankSortType) {
                    $scope.fullTankSortReverse = !$scope.fullTankSortReverse;
                } else {
                    $scope.fullTankSortType = header;
                    $scope.fullTankSortReverse = false;
                }
                $scope.updateFullTanks($scope.fullTankCurrentPage);
            };

        }
    ]);
