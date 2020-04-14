const moment = require("moment-timezone");

exports.currentDate = function() {
    return new Date();
}

exports.chatString = function(timestamp) {
    var myTimezone = "America/New_York";
    var myDatetimeFormat= "YYYY-MM-DD hh:mm:ss a z";
    var myDatetimeString = moment(timestamp).tz(myTimezone).format(myDatetimeFormat);
    return (" à " + myDatetimeString); 
}

