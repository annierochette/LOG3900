const CountdownTimer = require("./timer");

class MatchManager {

    constructor() {
        this.answers = new Map();
        this.timers = new Map();
        this.matches = new Map();
    }

    get count() {
        return this.answers.size();
    }
      
    validateAnswer = function(matchId, answer) {
        if (this.answers.contains(matchId)) {
            return (this.answers.get(matchId) == answer); 
        }

        return false;
    }

    addAnswer = function(matchId, answer) {
        this.answers.set(matchId,  answer);
    }

    startTimer = function(matchId, duration) {
        this.timers.set(matchId, new CountdownTimer(duration))
    }

    stopTimer = function(matchId) {
        this.timers.get(matchId).stop();
    }

    deleteMatch = function(matchId) {
        this.answers.delete(matchId);
        this.timers.delete(matchId);
    }
}

class Singleton {

  constructor() {
      if (!Singleton.instance) {
          Singleton.instance = new MatchManager();
      }
  }

  getInstance() {
      return Singleton.instance;
  }
}

module.exports = Singleton;
