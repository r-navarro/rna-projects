angular.module('demo-hockey')
	.factory('VehicleService', ['$resource', 'DaoService',
    function($resource, DaoService) {
				
		list = function() {
			return DaoService.getData("/vehicle/list", 'GET').then(function(response){
				return response.data;
			});
		}
		
		save = function(vehicle) {
			return DaoService.getData("/vehicle/save", 'POST', vehicle).then(function(response){
				return response.data;
			});
		}
		
		return {
			list : list,
			save : save
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