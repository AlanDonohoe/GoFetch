function validateForm() {
				var x = document.forms["target_url"]["target_url"].value;
				if(x == null || x == "" || x == "http://" || x.length < 6) {
					alert("Please Enter Valid URL");
					return false;
				}

				var y = document.forms["target_url"]["user_id"].value;
				var atpos = y.indexOf("@");
				var dotpos = y.lastIndexOf(".");
				if(atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= y.length) {
					alert("Please Enter Valid Email");
					return false;
				}
			}

// object that takes the URLPlusSocialList data from server and transforms into Google Chart friendly version

function URLPlusSocialData(serverURLPlusSocialData){
	
	
	
	this.serverDataString = new String(serverURLPlusSocialData);
	
	
	this.googleChartArray = function(){
		
		var startOfURLData, endOfURLData;
		
		// now find limits of url data we are interested in....
		startOfURLData = serverDataString.indexOf('URL', 0);
		
		endOfURLData = serverDataString.indexOf('\"URL\":\"', startOfURLData);
		
		serverDataString.splice(startOfURLData, endOfURLData);
		
		
		}
	
	/*
	 * google data is this:
	 * 
	 * an array of arrays
	 * 
	 * each entry = ['date of social entry', 'Twitter', 'Google+', 'LinkedIn', 'Pinterest', 'Delicious', 'StumbleUpon',
	 * 
	 * var data = google.visualization.arrayToDataTable([
          ['Date', 'FaceBook Total', 	'Twitter',	'Google+', 'LinkedIn', 'Pinterest', 'Delicious', 'StumbleUpon'], 
          ['10-09-2012',  1000,      	400,		300,		20,			30,			40,				50],
          ['12-09-2012',  1170,    		460,		400,			20,			30,			40,			50],
          ['15-09-2012',  660,     		1120,		600,			20,			30,			40,			50],
          ['19-09-2012',  1030,   		540,		700,			20,			30,			40,			50]
        ]);

        var options = {
          title: 'Historical Social Data for: URL value'
        };
	 */
};