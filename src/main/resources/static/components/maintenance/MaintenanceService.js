angular.module('carManagement.maintenance')
	.factory('MaintenanceService', ['$resource', 'DaoService',
    function($resource, DaoService) {
				
		list = function(vehicleId, page) {
			if(page){
				return DaoService.getData("/vehicles/"+vehicleId+"/maintenances/?page="+(page-1)+"&sort=predictedDate&name.dir=desc", 'GET').then(function(response){
					return response.data;
				});
			}else{
				return DaoService.getData("/vehicles/"+vehicleId+"/maintenances/", 'GET').then(function(response){
					return response.data;
				});
			}
		}
		
		save = function(vehicleId, maintenance) {
			return DaoService.getData("/vehicles/"+vehicleId+"/maintenances", 'POST', maintenance).then(function(response){
				return response.data;
			});
		}

		remove = function(vehicleId, maintenance) {
			return DaoService.getData("/vehicles/"+vehicleId+"/maintenances/"+maintenance.id, 'DELETE').then(function(response){
				return response.data;
			});
		}

		update = function(vehicleId, maintenance) {
			return DaoService.getData("/vehicles/"+vehicleId+"/maintenances/"+maintenance.id, 'PUT', maintenance).then(function(response){
				return response.data;
			});
		}

		get = function(vehicleId, idMaintenance){
		    return DaoService.getData("vehicles/"+vehicleId+"/maintenances/"+idMaintenance, 'GET').then(function(response){
		        return response.data;
		    });
		}

		costStats = function(vehicleId){
		    return DaoService.getData("vehicles/"+vehicleId+"/maintenances/costStats", 'GET').then(function(response){
		        return response.data;
		    });
		}

		distanceStats = function(vehicleId){
		    return DaoService.getData("vehicles/"+vehicleId+"/maintenance/distanceStats", 'GET').then(function(response){
		        return response.data;
		    });
		}

		return {
			list : list,
			save : save,
			update : update,
			remove : remove,
			get  : get,
			costStats : costStats,
			distanceStats : distanceStats
		}
		
	}
]);