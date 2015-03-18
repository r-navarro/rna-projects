angular.module('demo-hockey').controller('FullTankController',
		function($scope, FullTankService, $routeParams) {

			$scope.fullTank = {};
			$scope.vehicleId = $routeParams.vehicleId;

            $scope.create = function(fullTank){
                FullTankService.create(fullTank);
            }
		}
);