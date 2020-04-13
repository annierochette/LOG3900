const CountdownTimer = require("./timer");
const SOCKET = require("../../common/constants/socket");

class MatchTimer {
    constructor(matchId, duration, io) {
        this.matchId = matchId;
        this.remainingTime = duration;
        this.io = io;
        this.intervalHandler;
        this.duration = duration;
    }

    start() {
        var timer = this;
        let timeFlow = function() {
            this.remainingTime--;
            timer.io.to(this.matchId).emit(SOCKET.MATCH.REMAINING_TIME, this.remainingTime);

            if (this.remainingTime <= 0) {
                console.log("TIME IS OVER");
            }
        }
        
        this.intervalHandler = setInterval(timeFlow, 1000);
    }

    stop() {
        clearInterval(this.intervalHandler);
    }

    reset() {
        this.remainingTime = this.duration;
    }

    getRemainingTime() {
        return this.remainingTime;
    }
}

module.exports = MatchTimer;