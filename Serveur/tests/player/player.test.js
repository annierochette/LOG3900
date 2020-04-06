const supertest = require("supertest");
const app = require("../../src/app");
const request = supertest(app);
const HTTP = require("../../common/constants/http");
const AVATAR = require("../constants/avatar");

const firstName = "Abraham";
const lastName = "Simpson";
const username = "Abe";
const password = "qwertyu";

const ownerFirstName = "Monty";
const ownerLastName = "Burns";
const ownerUsername = "burns";
const ownerPassword = "excellent!!!";

var ownerToken;
var token;


describe("Players Rest API", () => {
  beforeAll(async done => {
        // Create owner
        const owner = await request
        .post("/players")
        .send({
          firstName: ownerFirstName,
          lastName: ownerLastName,
          username: ownerUsername,
          password: ownerPassword
      });
      ownerToken = owner.body.player.token;

      done();
  });

  afterAll(async done => {
    // Close connection to db
    done();
  });

  it("should not create a new player if the password under 7 characters", async done => {
    const res = await request
    .post("/players")
    .send({
      firstName: firstName,
      lastName: lastName,
      username: username,
      password: "123456"
    });
    
  expect(res.statusCode).toEqual(HTTP.STATUS.CONFLICT);

  done();
  });

  it("should create a new player", async done => {
    const res = await request
      .post("/players/")
      .send({
        firstName: firstName,
        lastName: lastName,
        username: username,
        password: password,
        avatar: AVATAR.avatar
      });

    expect(res.statusCode).toEqual(HTTP.STATUS.CREATED);
    expect(res.body.player.firstName).toEqual(firstName);
    expect(res.body.player.lastName).toEqual(lastName);
    expect(res.body.player.username).toEqual(username);
    expect(res.body.player.password).not.toEqual(password);
    expect(res.body.player.avatar).toEqual(AVATAR.avatar);
    expect(res.body.player.token).toBeDefined();

    token = res.body.player.token;
    done();
  });

  it("should logout a connected player", async done => {
    const res = await request
      .patch("/players/" + username + "/logout")
      .set("Authorization", "Bearer " + token);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(res.text).toEqual("Token removed");

    done();
  });

  it("should login an existing player", async done => {
    const res = await request
      .post("/players/login")
      .send({
        username: username,
        password: password
      });

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(res.body.player.firstName).toEqual(firstName);
    expect(res.body.player.lastName).toEqual(lastName);
    expect(res.body.player.username).toEqual(username);
    expect(res.body.player.password).not.toEqual(password);
    expect(res.body.player.avatar).toEqual(AVATAR.avatar);
    expect(res.body.player.token).toBeDefined();

    token = res.body.player.token;

    done();
  });

  it("should get all personnal informations", async done => {
    const res = await request
      .get("/players/" + ownerUsername)
      .set("Authorization", "Bearer " + ownerToken);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(res.body.player.firstName).toEqual(ownerFirstName);
    expect(res.body.player.lastName).toEqual(ownerLastName);
    expect(res.body.player.username).toEqual(ownerUsername);
    expect(res.body.player.password).not.toEqual(ownerPassword);
    expect(res.body.player.token).toBeDefined();

    done();
  });

  it("should get filtered informations on other player" ,async done => {
    const res = await request
    .get("/players/" + username)
    .set("Authorization", "Bearer " + ownerToken);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(res.body.player.firstName).not.toBeDefined();
    expect(res.body.player.lastName).not.toBeDefined();
    expect(res.body.player.username).toEqual(username);
    expect(res.body.player.password).not.toBeDefined();
    expect(res.body.player.token).not.toBeDefined();

    done();
  });

  it("should change the avatar", async done => {
    const res = await request
      .patch("/players/" + username + "/avatar")
      .set("Authorization", "Bearer " + token)
      .send({ avatar: AVATAR.empty });

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);

    done();
  });

  it("should delete an existing player", async done => {
    const res = await request
      .delete("/players/" + username);

    const owner = await request
      .delete("/players/" + ownerUsername);

    expect(res.statusCode).toEqual(HTTP.STATUS.OK);
    expect(owner.statusCode).toEqual(HTTP.STATUS.OK);

    done();
  });
});
