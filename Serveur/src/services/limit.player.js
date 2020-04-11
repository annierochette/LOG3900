const HTTP = require("../../common/constants/http");
const LOGGER = require("../utils/logger");
const MissingAuthorizationError = require("../errors/missing.authorization");
const MatchController = require("../match/match.controller");

const limitPlayer = async function(req, res, next) {
    try {
        console.log("limitPlayer");
        await MatchController.countPlayersInMatch(req.params.name);
        console.log("AFTER")
        next();
    } catch (error) {
        LOGGER.error(error);
        res.status(HTTP.STATUS.CONFLICT).send({ error: error.message });
    }
};

module.exports = limitPlayer;