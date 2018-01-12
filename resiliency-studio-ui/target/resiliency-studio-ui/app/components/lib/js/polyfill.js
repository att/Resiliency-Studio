if (!String.prototype.includes) {
		  String.prototype.includes = function(search, start) {
		    'use strict';
		    if (typeof start !== 'number') {
		      start = 0;
		    }
		    if(search != undefined || search != null){
		    
			    if (start + search.length > this.length) {
			      return false;
			    } else {
			      return this.indexOf(search, start) !== -1;
			    }
		    }
		  };
		}