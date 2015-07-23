angular.module('demo-hockey')
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