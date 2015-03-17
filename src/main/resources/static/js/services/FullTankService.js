angular.module('demo-hockey')
	.factory('FullTankService', ['$resource', 'DaoService',
    function($resource, DaoService) {
				
		list = function(vehicleId, page) {
			if(page){
				return DaoService.getData("/fullTank/list/"+vehicleId+ "/" +page, 'GET').then(function(response){
					return response.data;
				});
			}else{
				return DaoService.getData("/fullTank/list/"+vehicleId, 'GET').then(function(response){
					return response.data;
				});
			}
		}
		
		save = function(fullTank) {
			return DaoService.getData("/fullTank/save", 'POST', fullTank).then(function(response){
				return response.data;
			});
		}

		get = function(idFullTank){
		    return DaoService.getData("/fullTank/get/"+idFullTank, 'GET').then(function(response){
		        return response.data;
		    });
		}

		return {
			list : list,
			save : save,
			get  : get
		}
		
	}
]);