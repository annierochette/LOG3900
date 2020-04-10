class Timer {        
    constructor(io) {
        this.intervalHandler;
        this.io = io
    }

    start(timeFlow) {
        this.intervalHandler = setInterval(timeFlow, 1000);
    }

    stop() {
        clearInterval(this.intervalHandler);
    }

    reset() {
        clearInterval(this.intervalHandler);
    }
}

module.exports = Timer;
