angular.module('demo-hockey').controller('VehicleController',
		function($scope, VehicleService, DaoService) {

			$scope.vehicles;
			$scope.totalPages = 0;
			$scope.currentPage = 1;
			$scope.totalElements = 0;
			$scope.itemsPerPage = 0;

			$scope.vehicle = {};
			
			$scope.updateList = function(page){
				VehicleService.list(page).then(function(list){
					$scope.vehicles = list.content;
					$scope.totalPages = list.totalPages;
					$scope.totalElements = list.totalElements;
					$scope.itemsPerPage = list.size;
				});
			}

			$scope.pageChanged = function() {
                console.log('Page changed to: ' + $scope.currentPage);
                $scope.updateList($scope.currentPage);
             };

			$scope.create = function(vehicle){
				VehicleService.save(vehicle);
				
			}
			
			$scope.get = function(id){
			}
			

		}

);