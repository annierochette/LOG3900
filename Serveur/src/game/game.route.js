const express = require("express");
const CONTROLLER = require("./game.controller");
const authentification = require("../services/authentification");

const router = express.Router();

router.post("/games", CONTROLLER.createGame);
router.delete("/games/:name", CONTROLLER.deleteGame);
router.get("/games/:name", authentification, CONTROLLER.getGame);

module.exports = router;