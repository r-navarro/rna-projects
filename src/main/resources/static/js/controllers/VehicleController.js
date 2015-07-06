angular.module('demo-hockey').controller('VehicleController',
		function($scope, VehicleService, $routeParams, $location) {

			$scope.vehicles;
			$scope.totalPages = 0;
			$scope.currentPage = 1;
			$scope.totalElements = 0;
			$scope.itemsPerPage = 0;

			$scope.updateList = function(page){
				VehicleService.list(page).then(function(list){
					$scope.vehicles = list.content;
					$scope.totalPages = list.totalPages;
					$scope.totalElements = list.totalElements;
					$scope.itemsPerPage = list.size;
				});
			}

			$scope.pageChanged = function() {
                $scope.updateList($scope.currentPage);
             };

			$scope.create = function(vehicle){
				VehicleService.save(vehicle);
				$location.path('/list');
			}

		}
);