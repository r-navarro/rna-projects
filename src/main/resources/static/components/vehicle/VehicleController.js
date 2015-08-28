angular.module('carManagement.vehicle', ['ngRoute'])

.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/create', {
		templateUrl : 'components/vehicle/create.html',
		controller : 'VehicleController'
	}).when('/list', {
		templateUrl : 'components/vehicle/list.html',
		controller : 'VehicleController'
	}).when('/show/:vehicleId', {
        templateUrl : 'components/vehicle/show.html',
        controller : 'VehicleBoardController'
    });
}])



.controller('VehicleController',
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
				VehicleService.save(vehicle).then(function(){
					$location.path('/list');
				});				
			}

			$scope.deleteVehicle = function(id){
				VehicleService.remove(id).then(function(data){
					$scope.updateList(1);
				});	
			}

		}
);