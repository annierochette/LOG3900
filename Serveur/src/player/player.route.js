const express = require('express')
const authentification = require("../services/authentification");
const CONTROLLER = require("./player.controller");
const playerFilter = require("../services/player.filter");

const router = express.Router()

router.post("/players", CONTROLLER.createPlayer);
router.delete("/players/:pseudo", CONTROLLER.deletePlayer);
router.get("/players/:pseudo", authentification, playerFilter, CONTROLLER.getSinglePlayerInfos);
router.post("/players/login", CONTROLLER.login);
router.post("/players/logout", authentification, CONTROLLER.logout);

module.exports = router