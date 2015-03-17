angular.module('demo-hockey').controller('VehicleBoardController',
		function($scope, VehicleService, FullTankService, $routeParams) {

			$scope.fullTankTotalPages = 0;
			$scope.fullTankCurrentPage = 1;
			$scope.fullTankTotalElements = 0;
			$scope.fullTankItemsPerPage = 0;

			$scope.vehicleId = $routeParams.vehicleId;
			$scope.vehicle;
			$scope.fullTanks;

			$scope.updateFullTanks = function(page){
				FullTankService.list($scope.vehicleId, page).then(function(list){
					$scope.fullTanks = list.content;
					$scope.fullTankTotalPages = list.totalPages;
					$scope.fullTankTotalElements = list.totalElements;
					$scope.fullTankItemsPerPage = list.size;
				});
			};

			$scope.fullTankPageChanged = function() {
                $scope.updateFullTanks($scope.fullTankCurrentPage);
             };

			$scope.get = function(){
                VehicleService.get($scope.vehicleId).then(function(vehicle){
                    $scope.vehicle = vehicle;
                });
			}
			

		}

);