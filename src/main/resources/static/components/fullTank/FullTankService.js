angular.module('carManagement.fullTank')
    .factory('FullTankService', ['$resource', 'DaoService',
        function($resource, DaoService) {

            list = function(vehicleId, page, sort, dir) {
                sortString = DaoService.getSortString(sort, dir, "date");
                if (page) {
                    return DaoService.getData("/vehicles/" + vehicleId + "/fullTanks/?page=" + (page - 1) + "&" + sortString, 'GET').then(function(response) {
                        return response.data;
                    });
                } else {
                    return DaoService.getData("/vehicles/" + vehicleId + "/fullTanks/?" + sortString, 'GET').then(function(response) {
                        return response.data;
                    });
                }
            };

            save = function(vehicleId, fullTank) {
                return DaoService.getData("/vehicles/" + vehicleId + "/fullTanks", 'POST', fullTank).then(function(response) {
                    return response.data;
                });
            };

            remove = function(vehicleId, fullTank) {
                return DaoService.getData("/vehicles/" + vehicleId + "/fullTanks/" + fullTank.id, 'DELETE').then(function(response) {
                    return response.data;
                });
            };

            update = function(vehicleId, fullTank) {
                return DaoService.getData("/vehicles/" + vehicleId + "/fullTanks/" + fullTank.id, 'PUT', fullTank).then(function(response) {
                    return response.data;
                });
            };

            get = function(vehicleId, idFullTank) {
                return DaoService.getData("vehicles/" + vehicleId + "/fullTanks/" + idFullTank, 'GET').then(function(response) {
                    return response.data;
                });
            };

            costStats = function(vehicleId) {
                return DaoService.getData("vehicles/" + vehicleId + "/fullTanks/costStats", 'GET').then(function(response) {
                    return response.data;
                });
            };

            distanceStats = function(vehicleId) {
                return DaoService.getData("vehicles/" + vehicleId + "/fullTanks/distanceStats", 'GET').then(function(response) {
                    return response.data;
                });
            };

            averageStats = function(vehicleId) {
                return DaoService.getData("vehicles/" + vehicleId + "/fullTanks/averageStats", 'GET').then(function(response) {
                    return response.data;
                });
            };

            return {
                list: list,
                save: save,
                update: update,
                remove: remove,
                get: get,
                costStats: costStats,
                distanceStats: distanceStats,
                averageStats: averageStats
            };

        }
    ]);
