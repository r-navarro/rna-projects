angular.module('demo-hockey').controller('VehicleBoardController',
		function($scope, VehicleService, $routeParams) {

			$scope.totalPages = 0;
			$scope.currentPage = 1;
			$scope.totalElements = 0;
			$scope.itemsPerPage = 0;

			$scope.vehicle;
			$scope.fullTanks;

			$scope.pageChanged = function() {
                $scope.updateList($scope.currentPage);
             };

			$scope.get = function(){
			    id = $routeParams.vehicleId;
                VehicleService.get(id).then(function(vehicle){
                    $scope.vehicle = vehicle;
                });
			}
			

		}

);