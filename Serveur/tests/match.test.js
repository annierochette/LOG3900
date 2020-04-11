const MatchManager = require("../src/match/match.manager");
const app = require("../src/app");
const SocketIo = require('socket.io');
const http = require('http');
const DB = require("../src/db/db");

var matchManager;
const matchId = "matchId";
let players = ["playerA", "playerB", "playerC"];
let httpServer;
let io;

describe("MatchManager", () => {
    beforeAll(() => {
        DB.connect(process.env.MONGODB_URL, "matchManager");
        httpServer = http.createServer(app);
        io = SocketIo.listen(httpServer);
        if (io) {console.log("Exist")} else {console.log("NOTONONT")}
        matchManager = new MatchManager(io).getInstance();
      });

    afterAll(() => {
        matchManager.deleteMatch(matchId);
    });

    it("should add a match", () => {
        expect(matchManager.count()).toBe(0);
        matchManager.addMatch(matchId, players);
        expect(matchManager.count()).toBe(1);
    });

    it("should validate answer", () => {
        let dog = "chien";
        let cat = "cat";
        matchManager.addAnswer(matchId, dog);
        let validation = matchManager.validateAnswer(matchId, dog, players[0]);
        expect(validation.valid).toBe(true);
        expect(validation.score).toBe(210);
        
        matchManager.addAnswer(matchId, cat);
        matchManager.startTimer(matchId);
        setTimeout(() => {}, 2000);
        validation = matchManager.validateAnswer(matchId, cat, players[1]);
        expect(validation.valid).toBe(true);
        expect(validation.score).toBeLessThan(210);
        expect(validation.score).toBeGreaterThan(120);
        matchManager.stopTimer(matchId);
    });

    it("should manipulate the timer", () => {
        matchManager.startTimer(matchId);
        setTimeout(() => {
            matchManager.stopTimer();
            expect(matchManager.remainingTime(matchId)).toBe(85);
        }, 5000);
    });

    it("should switch role", async () => {
        let round = await matchManager.nextRound(matchId);
        expect(round.draftsman).toBe(players[0]);
        round = await matchManager.nextRound(matchId);
        expect(round.draftsman).toBe(players[1]);
        round = await matchManager.nextRound(matchId);
        expect(round.draftsman).toBe(players[2]);
        round = await matchManager.nextRound(matchId);
        expect(round.draftsman).toBe(players[0]);
    });
});