angular.module('demo-hockey')
	.factory('VehicleService', ['$resource', 'DaoService',
    function($resource, DaoService) {
				
		list = function(page) {
			if(page){
				return DaoService.getData("/vehicles/?page="+(page-1), 'GET').then(function(response){
					return response.data;
				});
			}else{
				return DaoService.getData("/vehicles/list", 'GET').then(function(response){
					return response.data;
				});
			}
		}
		
		save = function(vehicle) {
			return DaoService.getData("/vehicles", 'POST', vehicle).then(function(response){
				return response.data;
			});
		}

		get = function(idVehicle){
		    return DaoService.getData("/vehicles/"+idVehicle, 'GET').then(function(response){
		        return response.data;
		    });
		}

		return {
			list : list,
			save : save,
			get  : get
		}
		
	// urlService = '/vehicle';
	// return $resource(urlService, {vehicleId : '@id'},
	// {
	// 'update' : {method : 'PUT'},
	// 'list' : {url:urlService + '/list', method : 'GET', isArray:true},
	// 'create' : {url:urlService + '/save', method:'POST'},
	// 'delete' : {url:urlService + '/delete', method:'DELETE'},
	// 'get' : {url:urlService + '/get/:vehicleId', method:'GET'}
	// }
	//			);
		}
	]);