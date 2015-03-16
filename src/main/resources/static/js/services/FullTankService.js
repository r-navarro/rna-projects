angular.module('demo-hockey')
	.factory('FullTankService', ['$resource', 'DaoService',
    function($resource, DaoService) {
				
		list = function(page) {
			if(page){
				return DaoService.getData("/vehicle/list/"+page, 'GET').then(function(response){
					return response.data;
				});
			}else{
				return DaoService.getData("/vehicle/list", 'GET').then(function(response){
					return response.data;
				});
			}
		}
		
		save = function(vehicle) {
			return DaoService.getData("/vehicle/save", 'POST', vehicle).then(function(response){
				return response.data;
			});
		}

		get = function(idVehicle){
		    return DaoService.getData("/vehicle/get/"+idVehicle, 'GET').then(function(response){
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