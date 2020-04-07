class Timer {        
    constructor() {
        this.intervalHandler;
    }

    start = function(timeFlow) {
        intervalHandler = setInterval(timeFlow, 1000);
    }

    stop = function() {
        clearInterval(intervalHandler);
    }

    reset = function() {
        clearInterval(intervalHandler);
    }
}

module.exports = Timer;
