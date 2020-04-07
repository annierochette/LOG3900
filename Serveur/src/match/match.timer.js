const CountdownTimer = require("./timer");
const SOCKET = require("../../common/constants/socket");

class MatchTimer {
    constructor(matchId, duration, io) {
        this.matchId = matchId;
        this.timer = new CountdownTimer();
        this.remainingTime = duration;
        this.io = io;
    }

    start = function() {
        let timeFlow = function() {
            this.remainingTime--;
            io.to(matchId).emit(SOCKET.MATCH.REMAINING_TIME, this.remainingTime);
        }
        this.timer.start(timeFlow);
    }

    stop = function() {
        this.timer.stop();
    }

    reset = function() {
        this.timer.reset();
    }

    remainingTime = function() {
        return remainingTime;
    }
}