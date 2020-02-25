const supertest = require("supertest");
const app = require("../../src/app");
const request = supertest(app);
const HTTP = require("../../common/constants/http");

const firstName = "General";
const lastName = "Statistics";
const username = "generalStats";
const password = "ilovemaths";

var token;

describe("General Statistics REST API", () => {
  beforeAll(async done => {
        // Create player
        const player = await request
        .post("/players")
        .send({
          firstName: firstName,
          lastName: lastName,
          username: username,
          password: password
      });

      token = player.body.player.token;

      done()
  });

  afterAll(async done => {
    const res = await request
        .delete("/players/" + username); 

    // Close connection to db
    done();
  });

  it("should get general statistics", async done => {
    const res = await request
      .get("/players/" + username + "/general-statistics")
      .set("Authorization", "Bearer " + token);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    
    done();
  });
});
