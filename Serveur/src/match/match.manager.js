const MatchTimer = require("./match.timer");
const gameController = require("../game/game.controller"); 

class Match {
    constructor(players, matchId, duration, io) {
        this.players = players;
        this.scores = new Map();
        players.forEach(player => {
            this.scores.set(player, 0);
        });
        
        this.answers = [];
        this.timer = new MatchTimer(matchId, duration, io);
        
        this.actualRound = -1;
        this.goodAnswers = 0;
        this.maxRounds = players.length * 3;
    }

    updateScore(score, player) {
        if (this.scores.has(player)) {
            this.scores.set(player, this.scores.get(player) + score);
        } else {
            this.scores.set(player, score);
        }
    }

    validateAnswer(answer, player) {
        if (this.answers[this.answers.length - 1] == answer) {
            this.goodAnswers++;
            let score = this.calculateScore(player);

            return { "valid": true, "score": score };
        }

        return { "valid": false };
    }

    calculateScore(player) {
        let score = 120/this.goodAnswers + this.timer.getRemainingTime();
        
        this.updateScore(score, player);
        this.updateScore(75, this.players[this.actualRound % this.players.length]);

        return score;
    }

    addAnswer(answer) {
        this.answers.push(answer);
    }

    startTimer() {
        this.timer.start();
    }

    stopTimer() {
        this.timer.stop();
        this.timer.reset();
    }

    remainingTime() {
        this.timer.remainingTime();
    }

    async nextRound() {
        this.actualRound++;

        if (this.actualRound > this.maxRounds) {
            return { "status": "Completed" };
        }

        let game = await gameController.getRandomGame(this.answers);

        let round = {
            "draftsman": this.players[this.actualRound % this.players.length],
            "game": game,
            "status": "ongoing" 
        } 
        return round;
    }
}

class MatchManager {

    constructor(io) {
        this.matches = new Map();
        this.io = io;
        this.waitingRoom = new Map();
    }

    count() {
        return this.matches.size;
    }

    addMatch(matchId, players) {
        this.matches.set(matchId, new Match(players, matchId, 90, this.io));
    }
      
    validateAnswer(matchId, answer, player) {
        return this.matches.get(matchId).validateAnswer(answer, player);
    }

    addAnswer(matchId, answer) {
        this.matches.get(matchId).addAnswer(answer);
    }

    startTimer(matchId, duration) {
        this.matches.get(matchId).startTimer();
    }

    stopTimer(matchId) {
        this.matches.get(matchId).stopTimer();
    }

    deleteMatch(matchId) {
        this.matches.delete(matchId);
    }

    remainingTime(matchId) {
        return this.matches.get(matchId).remainingTime();
    }

    async nextRound(matchId) {
        return await this.matches.get(matchId).nextRound();
    }

    addPlayerToWaitingRoom(matchId, username) {
        if (this.waitingRoom.has(matchId)) {
            this.waitingRoom.get(matchId).push(username);
        } else {
            this.waitingRoom.set(matchId, [username]);
        }

        return this.waitingRoom.get(matchId); 
    }

    getPlayerInWaitingRoom(matchId) {
        return this.waitingRoom.get(matchId);
    }
}

class Singleton {

  constructor(io) {
      if (!Singleton.instance) {
          Singleton.instance = new MatchManager(io);
      }
  }

  getInstance() {
      return Singleton.instance;
  }
}

module.exports = Singleton;
