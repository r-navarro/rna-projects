angular.module('carManagement')
    .factory('DaoService', ['$http', '$location',
        function($http, $location) {

            function getData(urlAction, method, data) {
                var req = {
                    method: method.toUpperCase(),
                    url: urlAction,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data: data
                };

                var promise = $http(req);

                promise.then(success, failure);
                return promise;
            }

            function success(response) {
                $http.post("/stats/ok", response);
            }

            function failure(response) {
                $http.post("/stats/ko", response);
                if (responce.status == 401) {
                    $location.path("/login");
                }
            }

            return {
                getData: getData
            };
        }
    ]);
