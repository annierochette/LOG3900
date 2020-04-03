const express = require("express");
const CONTROLLER = require("./quickdraw.controller");
const authentification = require("../services/authentification");

const router = express.Router();

router.get("/games/quickdraw", authentification, CONTROLLER.getRandomDrawings);
router.get("/games/quickdraw/:category", authentification, CONTROLLER.getCategory)

module.exports = router;