angular.module('demo-hockey')
	.factory('ChartService', 
		function(){
			function drawLineChart(data, id) {

				// Set the dimensions of the canvas / graph
				var margin = {top: 30, right: 20, bottom: 30, left: 50},
				    width = 425 - margin.left - margin.right,
				    height = 270 - margin.top - margin.bottom;

				// Parse the date / time
				var parseDate = d3.time.format("%d/%m/%Y").parse;

				// Set the ranges
				var x = d3.time.scale().range([0, width]);
				var y = d3.scale.linear().range([height, 0]);

				// Define the axes
				var xAxis = d3.svg.axis().scale(x)
				    .orient("bottom").ticks(d3.time.week, 2).tickFormat(d3.time.format("%d/%m/%Y"));

				var yAxis = d3.svg.axis().scale(y)
				    .orient("left").ticks(5);

				// Define the line
				var valueline = d3.svg.line()
				    .x(function(d) { return x(d.date); })
				    .y(function(d) { return y(d.cost); });
				    
				// Adds the svg canvas
				var svg = d3.select(id)
				    .append("svg")
				        .attr("width", width + margin.left + margin.right)
				        .attr("height", height + margin.top + margin.bottom)
				    .append("g")
				        .attr("transform", 
				              "translate(" + margin.left + "," + margin.top + ")");

			   data.forEach(function(d) {
			        d.date = parseDate(d.date);
			        d.cost = +d.cost;
			    });

			    // Scale the range of the data
			    x.domain(d3.extent(data, function(d) { return d.date; }));
			    y.domain([0, d3.max(data, function(d) { return d.cost; })]);

			    // Add the valueline path.
			    svg.append("path")
			        .attr("class", "line")
			        .attr("d", valueline(data));

			    // Add the X Axis
			    svg.append("g")
			        .attr("class", "x axis")
			        .attr("transform", "translate(0," + height + ")")
			        .call(xAxis);

			    // Add the Y Axis
			    svg.append("g")
			        .attr("class", "y axis")
			        .call(yAxis);

			    // Add tooltips
			    var focus = svg.append("g").style("display", "none");

				focus.append("line")
				        .attr("class", "x")
				        .style("stroke", "blue")
				        .style("stroke-dasharray", "3,3")
				        .style("opacity", 0.5)
				        .attr("y1", 0)
				        .attr("y2", height);

				 // append the y line
				    focus.append("line")
				        .attr("class", "y")
				        .style("stroke", "blue")
				        .style("stroke-dasharray", "3,3")
				        .style("opacity", 0.5)
				        .attr("x1", width)
				        .attr("x2", width);

				    // append the circle at the intersection
				    focus.append("circle")
				        .attr("class", "y")
				        .style("fill", "none")
				        .style("stroke", "blue")
				        .attr("r", 4);

				    // place the value at the intersection
				    focus.append("text")
				        .attr("class", "y1")
				        .style("stroke", "white")
				        .style("stroke-width", "3.5px")
				        .style("opacity", 0.8)
				        .attr("dx", -25)
				        .attr("dy", "-.3em");
				    focus.append("text")
				        .attr("class", "y2")
				        .attr("dx", -25)
				        .attr("dy", "-.3em");

				    // place the date at the intersection
				    focus.append("text")
				        .attr("class", "y3")
				        .style("stroke", "white")
				        .style("stroke-width", "3.5px")
				        .style("opacity", 0.8)
				        .attr("dx", -50)
				        .attr("dy", "1em");
				    focus.append("text")
				        .attr("class", "y4")
				        .attr("dx", -50)
				        .attr("dy", "1em");

				// append the rectangle to capture mouse
				    svg.append("rect")
				        .attr("width", width)
				        .attr("height", height)
				        .style("fill", "none")
				        .style("pointer-events", "all")
				        .on("mouseover", function() { focus.style("display", null); })
				        .on("mouseout", function() { focus.style("display", "none"); })
				        .on("mousemove", mousemove);

					var bisectDate = d3.bisector(function(d) { return d.date; }).left,
				    formatDate = d3.time.format("%d/%m/%Y");

				    function mousemove() {
						var x0 = x.invert(d3.mouse(this)[0]),
						    i = bisectDate(data, x0, 1);

						if (i >= data.length -1) {
							i = data.length -1;
						}

						var d0 = data[i - 1],
						    d1 = data[i],
						    d = x0 - d0.date > d1.date - x0 ? d1 : d0;

						focus.select("circle.y")
						    .attr("transform",
						          "translate(" + x(d.date) + "," +
						                         y(d.cost) + ")");

						focus.select("text.y1")
						    .attr("transform",
						          "translate(" + x(d.date) + "," +
						                         y(d.cost) + ")")
						    .text(d.cost);

						focus.select("text.y2")
						    .attr("transform",
						          "translate(" + x(d.date) + "," +
						                         y(d.cost) + ")")
						    .text(d.cost);

						focus.select("text.y3")
						    .attr("transform",
						          "translate(" + x(d.date) + "," +
						                         y(d.cost) + ")")
						    .text(formatDate(d.date));

						focus.select("text.y4")
						    .attr("transform",
						          "translate(" + x(d.date) + "," +
						                         y(d.cost) + ")")
						    .text(formatDate(d.date));

						focus.select(".x")
						    .attr("transform",
						          "translate(" + x(d.date) + "," +
						                         y(d.cost) + ")")
						               .attr("y2", height - y(d.cost));

						focus.select(".y")
						    .attr("transform",
						          "translate(" + width * -1 + "," +
						                         y(d.cost) + ")")
						               .attr("x2", width + width);
					}
			}

			return {
				drawLineChart: drawLineChart
			}
		}
	);