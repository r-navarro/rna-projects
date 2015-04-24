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
                $scope.fullTank.date = new Date();
            };

			$scope.init();

            $scope.open = function($event) {
                $event.preventDefault();
                $event.stopPropagation();

                $scope.opened = true;
            };

            $scope.format = "dd/MM/yyyy";
		}
);