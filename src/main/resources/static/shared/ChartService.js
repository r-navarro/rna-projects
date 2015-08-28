angular.module('carManagement')
	.factory('ChartService', 
		function(){

			function drawLineChart(key, data) {
				return [
			    	{
			 	        "key": key,
			            "values": data
			 		}
			 	];
			}

			return {
				drawLineChart: drawLineChart
			}
		}
	);