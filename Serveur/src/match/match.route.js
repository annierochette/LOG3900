const express = require("express");
const CONTROLLER = require("./match.controller");
const authentification = require("../services/authentification");

const router = express.Router();

router.post("/match/:type", authentification, CONTROLLER.createMatch);
router.patch("/match/:name/player", authentification, CONTROLLER.addPlayer);
router.patch("/match/:name/score", authentification, CONTROLLER.updateScore);
router.patch("/match/:name/game", authentification, CONTROLLER.addGame);
router.patch("/match/:name/status", authentification, CONTROLLER.updateStatus);
router.get("/match/:name", authentification, CONTROLLER.getMatch);
router.get("/match/", authentification, CONTROLLER.getUnstartedMatch);

module.exports = router;