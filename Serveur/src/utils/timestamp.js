exports.currentDate = function() {
    return new Date();
}

exports.chatString = function(timestamp) {
    return (" à " + timestamp.getHours()
        + ":" + timestamp.getMinutes()
        + ":" + timestamp.getSeconds()); 
}
