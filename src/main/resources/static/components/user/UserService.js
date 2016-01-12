angular.module('carManagement.user')
    .factory('UserService', ['DaoService',
        function(DaoService) {

            get = function() {
                return DaoService.getData("users/", 'GET').then(function(response) {
                    return response.data;
                });
            };

            update = function(userId, user) {
                return DaoService.getData("users/" + userId, 'PUT', user).then(function(response) {
                    return response.data;
                });
            };

            create = function(user) {
                return DaoService.getData("users/", 'POST', user).then(function(response) {
                    return response.data;
                });
            };

            return {
                get: get,
                update: update,
                create: create
            };
        }
    ]);
