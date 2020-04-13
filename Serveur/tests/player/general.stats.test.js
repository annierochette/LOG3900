const supertest = require("supertest");
const app = require("../../src/app");
const request = supertest(app);
const HTTP = require("../../common/constants/http");
const DB = require("../../src/db/db");

const firstName = "General";
const lastName = "Statistics";
const username = "generalStats";
const password = "ilovemaths";

const loserUsername = "loser"

var token;
var loserToken;

describe("General Statistics REST API", () => {
  beforeAll(async done => {
        // Create player
        DB.connect(process.env.MONGODB_URL, "generalStats");
        const player = await request
        .post("/players")
        .send({
          firstName: firstName,
          lastName: lastName,
          username: username,
          password: password
      });

      token = player.body.player.token;

      const loser = await request
      .post("/players")
      .send({
        firstName: firstName,
        lastName: lastName,
        username: loserUsername,
        password: password
    });

    loserToken = loser.body.player.token;
    
    done()
  });

  afterAll(async done => {
    await request.delete("/players/" + username);
    await request.delete("/players/" + loserUsername); 
    DB.disconnect();
    done();
  });

  it("should get general statistics", async done => {
    const res = await request
      .get("/players/" + username + "/general-statistics")
      .set("Authorization", "Bearer " + token);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(res.body.generalStats.username).toEqual(username);
    expect(res.body.generalStats.matchPlayed).toEqual(0);
    expect(res.body.generalStats.matchWon).toEqual(0);

    done();
  });

  it("should increase the number of matches won by one", async done => {
    const res = await request
        .patch("/players/stats")
        .send({ matchResult: { winner: username, players: [username, loserUsername] } });
    
    const getRes = await request
        .get("/players/" + username + "/general-statistics")
        .set("Authorization", "Bearer " + token);

    const loserStats = await request
        .get("/players/" + loserUsername + "/general-statistics")
        .set("Authorization", "Bearer " + loserToken);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(getRes.body.generalStats.matchWon).toEqual(1);
    expect(getRes.body.generalStats.matchPlayed).toEqual(1);
    expect(loserStats.body.generalStats.matchWon).toEqual(0);
    expect(loserStats.body.generalStats.matchPlayed).toEqual(1);

    done();
  });
});
