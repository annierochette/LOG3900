const express = require("express");
const CONTROLLER = require("./match.controller");
const authentification = require("../services/authentification");
const nameGenerator = require("../services/name.generator");
const limitPlayer = require("../services/limit.player");

const router = express.Router();

router.post("/match/:type", authentification, nameGenerator, CONTROLLER.createMatch);
router.patch("/match/:name/player", authentification, limitPlayer, CONTROLLER.addPlayer);
router.patch("/match/:name/score", authentification, CONTROLLER.updateScore);
router.patch("/match/:name/game", authentification, CONTROLLER.addGame);
router.patch("/match/:name/status", authentification, CONTROLLER.updateStatus);
router.get("/match/:name", authentification, CONTROLLER.getMatch);
router.get("/match/", authentification, CONTROLLER.getUnstartedMatch);

module.exports = router;