angular.module('carManagement.fullTank', ['ngRoute'])

.config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/:vehicleId/ft/create', {
        templateUrl : 'components/fullTank/create.html',
        controller : 'FullTankController'
    }).when('/:vehicleId/ft/update/:ftId', {
        templateUrl : 'components/fullTank/update.html',
        controller : 'FullTankController'
    });
}])

.controller('FullTankController',
		function($scope, FullTankService, VehicleService, $routeParams, $location) {

			$scope.fullTank = {};

			$scope.vehicleId = $routeParams.vehicleId;

            $scope.ftId = $routeParams.ftId;

            $scope.create = function(fullTank){
                FullTankService.save($scope.vehicleId, fullTank).then(function(){
                    $location.path('/show/'+$scope.vehicleId);
                });
            };

            $scope.update = function(fullTank){
                FullTankService.update($scope.vehicleId, fullTank).then(function(){
                    $location.path('/show/'+$scope.vehicleId);
                });
            };
           
            $scope.init = function(){
                if($scope.ftId){
                    FullTankService.get($scope.vehicleId, $scope.ftId).then(function(ft){
                        $scope.fullTank = ft;
                        $scope.fullTank.date = new Date(ft.date);
                    });
                } else {
                    $scope.fullTank.date = new Date();
                }
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