angular.module('demo-hockey').controller('FullTankController',
		function($scope, FullTankService, VehicleService, $routeParams) {

			$scope.fullTank = {
			    vehicle:$routeParams.vehicleId
			};

			$scope.vehicleId = $routeParams.vehicleId;

            $scope.create = function(fullTank){
                FullTankService.save(fullTank);
            };

            $scope.init = function(){
                VehicleService.get($scope.vehicleId).then(function(vehicle){
                    $scope.fullTank.vehicle = vehicle;
                });
            };

			$scope.init();
		}
);