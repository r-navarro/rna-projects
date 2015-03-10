angular.module('demo-hockey').controller('VehicleController',
		function($scope, VehicleService, DaoService) {

			$scope.vehicles;
			
			function updateList(){
				VehicleService.list().then(function(list){
					$scope.vehicles = list;
				});
			}

			$scope.create = function(vehicle){
				VehicleService.save(vehicle).then(function(){
					updateList();
				});
				
			}
			
			$scope.get = function(id){
				//VehicleService.get(id);
				//console.log(id);
			}
			
			updateList();

		}

);