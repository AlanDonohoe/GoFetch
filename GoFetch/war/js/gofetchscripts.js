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