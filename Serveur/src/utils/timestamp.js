exports.currentDate = function() {
    return new Date();
}

exports.chatString = function() {
    let timestamp = new Date();
    return (" à " + timestamp.getHours()
        + ":" + timestamp.getMinutes()
        + ":" + timestamp.getSeconds()); 
}
