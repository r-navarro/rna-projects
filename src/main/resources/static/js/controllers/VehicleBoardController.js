angular.module('demo-hockey').controller('VehicleBoardController',
	function($scope, VehicleService, FullTankService, $routeParams, ChartService, $location) {

		$scope.fullTankTotalPages = 0;
		$scope.fullTankCurrentPage = 1;
		$scope.fullTankTotalElements = 0;
		$scope.fullTankItemsPerPage = 0;

		$scope.vehicleId = $routeParams.vehicleId;
		$scope.vehicle;
		$scope.fullTanks;
		$scope.distanceData;
		$scope.costData;

		$scope.updateFullTanks = function(page){
			FullTankService.list($scope.vehicleId, page).then(function(list){
				$scope.fullTanks = list.content;
				$scope.fullTankTotalPages = list.totalPages;
				$scope.fullTankTotalElements = list.totalElements;
				$scope.fullTankItemsPerPage = list.size;
			});
		};

		$scope.deleteFullTank = function(fullTank){
		    FullTankService.remove($scope.vehicleId, fullTank);
		    $location.path('/show/'+$scope.vehicleId);
		    $scope.get();
		    $scope.updateFullTanks(1);
		};

		$scope.fullTankPageChanged = function() {
            $scope.updateFullTanks($scope.fullTankCurrentPage);
         };

		$scope.get = function(){
            VehicleService.get($scope.vehicleId).then(function(vehicle){
                $scope.vehicle = vehicle;
            });

            var formatDate = d3.time.format("%d/%m/%Y").parse;

			FullTankService.costStats($scope.vehicleId).then(function(data){
				data.forEach(function(d) {
			        d[0] = formatDate(d[0]);
			    });
				$scope.costData = ChartService.drawLineChart("Cost", data);
			});

			FullTankService.distanceStats($scope.vehicleId).then(function(data){				
				data.forEach(function(d) {
			        d[0] = formatDate(d[0]);
			    });
				$scope.distanceData = ChartService.drawLineChart("Distance", data);
			});
		}

		$scope.xAxisTickFormat_Date_Format = function() {
			return function(d){
		    	return d3.time.format("%d/%m/%Y")(new Date(d)); 
		    }
		}


	}
);