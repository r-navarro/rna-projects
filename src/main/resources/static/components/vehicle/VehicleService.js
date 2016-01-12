angular.module('carManagement.vehicle')
    .factory('VehicleService', ['$resource', 'DaoService',
        function($resource, DaoService) {

            list = function(page) {
                if (page) {
                    return DaoService.getData("/vehicles/?page=" + (page - 1), 'GET').then(function(response) {
                        return response.data;
                    });
                } else {
                    return DaoService.getData("/vehicles/list", 'GET').then(function(response) {
                        return response.data;
                    });
                }
            };

            save = function(vehicle, callback, errorCallback) {
                return DaoService.getData("/vehicles", 'POST', vehicle).then(function(response) {
                    callback();
                }, function(error) {
                    errorCallback(error.data.errorMessage);
                });
            };

            update = function(vehicle, callback, errorCallback) {
                return DaoService.getData("/vehicles", 'PUT', vehicle).then(function(response) {
                    callback();
                }, function(error) {
                    errorCallback(error.data.errorMessage);
                });
            };

            get = function(idVehicle) {
                return DaoService.getData("/vehicles/" + idVehicle, 'GET').then(function(response) {
                    return response.data;
                });
            };

            remove = function(idVehicle) {
                return DaoService.getData("/vehicles/" + idVehicle, 'DELETE').then(function(response) {
                    return response.data;
                });
            };

            return {
                list: list,
                save: save,
                get: get,
                remove: remove,
                update: update
            };

        }
    ]);
