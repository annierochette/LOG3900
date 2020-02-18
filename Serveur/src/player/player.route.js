const express = require('express')
const CONTROLLER = require("./player.controller");
const authentification = require("../services/authentification");
const playerFilter = require("../services/player.filter");
const ownerAuthentification = require("../services/owner.authentification");

const router = express.Router()

router.post("/players", CONTROLLER.createPlayer);
router.delete("/players/:username", CONTROLLER.deletePlayer);
router.get("/players/:username", authentification, playerFilter, CONTROLLER.getSinglePlayerInfos);
router.patch("/players/:username/avatar", ownerAuthentification, CONTROLLER.changeAvatar)
router.post("/players/login", CONTROLLER.login);
router.patch("/players/:username/logout", ownerAuthentification, CONTROLLER.logout);

module.exports = router