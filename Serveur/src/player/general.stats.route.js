const express = require('express')
const CONTROLLER = require("./general.stats.controller");
const authentification = require("../services/authentification");

const router = express.Router();

router.get("/players/:username/general-statistics", authentification, CONTROLLER.getGeneralStatistics);

module.exports = router;