class MatchManager {

    constructor() {
        this.answers = new Map();
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
        console.log("HEER")
        this.answers.set(matchId,  answer);
        console.log("HEER")
    }

    deleteMatch = function(matchId) {
        this.answers.delete(matchId);
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
