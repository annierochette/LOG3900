const express = require('express')
const Player = require("./player.model");
const authentification = require("../services/authentification");
const CONTROLLER = require("./player.controller");

const router = express.Router()

router.post("/players", CONTROLLER.createPlayer);
router.delete("/players/:pseudo", CONTROLLER.deletePlayer);
router.get("/players/:pseudo", authentification, CONTROLLER.getSinglePlayer);
router.post("/players/login", CONTROLLER.login);
router.post("/players/logout", authentification, CONTROLLER.logout);

module.exports = router