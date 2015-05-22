/**
 * Publishes these globals:
 * 
 * SERVER_CHARTS
 * 		Initialize charts with SERVER_CHARTS.initServerCharts(setupFunction);
 * 		Init websocket with SERVER_CHARTS.openWebSocket(openCommand);
 * 
 */
(function() {
	"use strict";
	
	var SERVER_CHARTS = new function() {
		
		/*
		 * Configuration for server charts
		 */
		
		// number of datapoints to show
		this.amount = 720;
		
		/*
		 * Chart state variables
		 */
		this.chartList = {};
		
		var _self = this;
		
		/*
		 * Configures a new chart and adds to the list of charts
		 * 
		 */
		this.updateChartDataOptions = function(config) {
			var servername = config.servername;
			var type = config.type;
			var name = config.name;
			var property = config.property;
			var cssId = config.id;
			
			var chartState = _self.registerChart(servername, type, name, property);
			
			chartState.data = new google.visualization.DataTable();
			chartState.data.addColumn("datetime", "Time");
			chartState.data.addColumn("number", name);
			chartState.options = { hAxis: { title: 'Time' }, vAxis: { title: property} };
			chartState.chart = new google.visualization.LineChart(document.getElementById(cssId));
			chartState.chart.draw(chartState.data, chartState.options);
		
			// retrieve data for this chart
			$.ajax({
				type: "GET",
				url: "http://" + window.location.host + "/rest/datapoint",
				dataType: "json",
				contentType: "application/json",
				success: function(jsondata) {
					
					_self.chartDataUpdate(chartState, jsondata);
				},
				data: {serverName: servername, dataName: name, dataType: type, dataProperty: property, amount: _self.amount}
			});
		};
		
		/*
		 * Initialize the state tracking for this chart
		 * 	If state for these properties does not exist yet
		 * 	OR if forceNew == true
		 * 
		 *  returns chart state object
		 */
		this.registerChart = function(servername, type, name, property, forceNew) {
			
			servername = servername || "undefined";
			type = type || "undefined";
			name = name || "undefined";
			property = property || "undefined";
			
			if (!_self.chartList.hasOwnProperty(servername)){
				_self.chartList[servername] = {};
			}
			
			if (!_self.chartList[servername].hasOwnProperty(type)){
				_self.chartList[servername][type] = {};
			}
			
			if (!_self.chartList[servername][type].hasOwnProperty(name)){
				_self.chartList[servername][type][name] = {};
			}
			
			if (!_self.chartList[servername][type][name].hasOwnProperty(property) || forceNew){
				_self.chartList[servername][type][name][property] = {
						chart: null,
						data: null,
						options: [],
						servername: servername,
						type: type,
						name: name,
						property: property,
						lastValue: null
				};
			}
			
			return _self.chartList[servername][type][name][property];
		}

		/*
		 * For a give chartState ({chart,data, options} object)
		 * 	update it with the found data
		 */
		this.chartDataUpdate = function(chartState, jsondata) {
			jsondata.reverse();
			
			for(var i = 0; i < jsondata.length; i++) {
				
				_self.addChartDataPoint(jsondata[i].dataValue, jsondata[i].dataTimeStamp, chartState);
				
			}
			
			if (jsondata.length > 0) {
				// update timestamp if div is available
				var lastUpdatedEl = document.getElementById(chartState.servername + "_lastupdated");
				if (lastUpdatedEl) {
					lastUpdatedEl.innerHTML = new Date(jsondata[jsondata.length - 1].dataTimeStamp * 1);
				}
			}
			
			chartState.chart.draw(chartState.data, chartState.options);
		};
		
		/*
		 * Initialize Google Charts API
		 */
		this.initServerCharts = function(setupFunction) {
			
			google.load('visualization', '1.1', {packages: ['corechart', 'line']});
			google.setOnLoadCallback(setupFunction);
			
		};
		
		/*
		 * Open a websocket using message as the open command
		 */
		this.openWebSocket = function(openCommand) {
			if ("WebSocket" in window) {
				var url = 'ws://' + window.location.host + '/websocket';
				console.log(url);
				var ws = new WebSocket(url);
				
				ws.onopen = function()  {
					console.log("Connection Opened");
					ws.send(openCommand);
				};
		
				ws.onmessage = _self.onWebSocketMessage;
		
				ws.onclose = function (evt) {
					console.log("Connection Closed");
				};
		
			} else {
				alert("WebSocket NOT supported by your Browser!");
			}
		};
		
		/*
		 * Receives websocket data
		 */
		this.onWebSocketMessage = function(evt) {
			if(evt.data == "session opened") return;
			var msg = JSON.parse(evt.data);
			
			var chartState = _self.registerChart(msg.serverName, msg.dataType, msg.dataName, msg.dataProperty);
			
			// if a chart exists with data of this type
			if(chartState.data && chartState.data.getNumberOfRows() > 0) {
				
				// update timestamp if div is available
				var lastUpdatedEl = document.getElementById(msg.serverName + "_lastupdated");
				if (lastUpdatedEl) {
					lastUpdatedEl.innerHTML = new Date(msg.dataTimeStamp * 1);
				}
				
				_self.addChartDataPoint(msg.dataValue, msg.dataTimeStamp, chartState);
				
				while(chartState.data.getNumberOfRows() > _self.amount) chartState.data.removeRow(0);
				chartState.chart.draw(chartState.data, chartState.data.options);
			}
		};
		
		/*
		 * Parse a data value for the given chart
		 * 	Formats for each chart type
		 */
		this.addChartDataPoint = function(value, timeStamp, chartState) {
			
			
			var formattedDate = new Date(timeStamp * 1);
		
			// Disk Speed
			if(chartState.type == "Disk" && chartState.property != "Speed") {
				
				var matchesArray = value.match(/(\d+)(\w)/);
				chartState.data.addRow([formattedDate, matchesArray[1] * 1]);
			
			// System Uptime
			} else if(chartState.type == "System" && chartState.name == "Uptime") {
				
				var matchesArray = value.match(/(\d+):.*/);
				chartState.data.addRow([formattedDate, matchesArray[1] * 1]);
				
			// Network
			} else if(chartState.type == "Network") {
				
				if(chartState.data.getNumberOfRows() > 0) {
					var lastTime = chartState.data.getValue(rows - 1, 0),
						last = chartState.lastValue || 0,
						timeDiff,
						amountDiff;
					
					if(lastTime != 0) {
						timeDiff = ((timeStamp * 1) - lastTime.getTime()) / 1000;
						amountdDiff = (value * 1) - last;
						chartState.data.addRow([formattedDate, (amoutnDiff / timeDiff)]);
					}
				}

			// Default
			} else {
				chartState.data.addRow([formattedDate, value * 1]);
			}

			chartState.lastValue = (value * 1);
		};
	}();
	
	
	
	// publish global functions
	window.SERVER_CHARTS = SERVER_CHARTS;
	
}());