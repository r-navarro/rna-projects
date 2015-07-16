angular.module('demo-hockey').controller('FullTankController',
		function($scope, FullTankService, VehicleService, $routeParams, $location) {

			$scope.fullTank = {};

			$scope.vehicleId = $routeParams.vehicleId;

            $scope.create = function(fullTank){
                FullTankService.save($scope.vehicleId, fullTank);
                $location.path('/show/'+$scope.vehicleId);
            };

            $scope.init = function(){
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