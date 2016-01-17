angular.module('carManagement')
    .factory('ChartService',
        function() {

            function drawLineChart(key, data) {

                var formatDate = d3.time.format("%d/%m/%Y").parse;
                var values = [];
                data.forEach(function(d) {
                    var array = [];
                    array.push(formatDate(d.date));
                    array.push(parseFloat(d.value));
                    values.push(array);
                });
                return [{
                    "key": key,
                    "values": values
                }];
            }

            return {
                drawLineChart: drawLineChart
            };
        }
    );
