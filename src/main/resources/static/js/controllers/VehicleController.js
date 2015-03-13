angular.module('demo-hockey').controller('VehicleController',
		function($scope, VehicleService, DaoService) {

			$scope.vehicles;
			$scope.totalPages = 0;

			$scope.vehicle = {};
			
			$scope.updateList = function(page){
				VehicleService.list(page).then(function(list){
					$scope.vehicles = list.content;
					$scope.totalPages = list.totalPages;
				});
			}

			$scope.create = function(vehicle){
				VehicleService.save(vehicle);
				
			}
			
			$scope.get = function(id){
			}
			

		}

);