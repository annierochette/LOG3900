const CountdownTimer = require("./timer");

class PlayerScore {
    constructor() {
        this.scores = new Map();
    }

    updateScore = function(score, player) {
        if (scores.contains(player)) {
            this.scores.set(player, this.scores.get(player) + score);
        } else {
            this.scores.set(player, score);
        }
    }
}

class MatchManager {

    constructor() {
        this.answers = new Map();
        this.timers = new Map();
        this.scores = new Map();
        this.goodAnswers = 0;
    }

    get count() {
        return this.answers.size();
    }
      
    validateAnswer = function(matchId, answer, player) {
        if (this.answers.contains(matchId)
            && this.answers.get(matchId) == answer) {
            this.goodAnswers++;
            let score = calculateScore(matchId, player);

            return { "valid": true, "": score };
        }

        return { "valid": false };
    }

    addAnswer = function(matchId, answer) {
        this.answers.set(matchId,  answer);
    }

    startTimer = function(matchId, duration) {
        this.timers.set(matchId, new CountdownTimer(duration));
        this.timers.get(matchId).startTimer();
    }

    stopTimer = function(matchId) {
        this.timers.get(matchId).stop();
    }

    deleteMatch = function(matchId) {
        this.answers.delete(matchId);
        this.timers.delete(matchId);
    }

    calculateScore = function(matchId, player) {
        let score = 120/this.goodAnswers + this.timers.get(matchId).remainingTime();
        let playerScore = this.scores.get(matchId);
        if (!playerScore) {
            playerScore = new PlayerScore();
        }
        
        playerScore.updateScore(score, player);

        return score;
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
