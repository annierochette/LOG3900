const express = require("express");
const CONTROLLER = require("./match.controller");
const authentification = require("../services/authentification");

const router = express.Router();

router.post("/match/:type", authentification, CONTROLLER.createMatch);
router.patch("/match/:name/player", authentification, CONTROLLER.addPlayer);
router.patch("/match/:name/score", authentification, CONTROLLER.updateScore);

module.exports = router;