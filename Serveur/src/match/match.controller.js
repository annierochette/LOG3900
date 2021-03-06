const HTTP = require("../../common/constants/http");
const ERR = require("../errors/messages/err");
const LOGGER = require("../utils/logger");
const Match = require("./match.model");
const MatchManager = require("../match/match.manager");
const Timestamp = require("../utils/timestamp");

exports.createMatch = async function(req, res) {
    try {         
        let match = new Match(req.body);
        await match.save();
        res.status(HTTP.STATUS.CREATED).json({ match });
    } catch (error) {
        LOGGER.info(error);
        res.status(HTTP.STATUS.CONFLICT).send(error);
    }
}

exports.addPlayer = async function(req, res) {
    try {         
        await Match.findOneAndUpdate(
            { name: req.params.name },
            { $push: { players: { name: req.body.player } } }
        );
        res.status(HTTP.STATUS.OK).end();
    } catch (error) {
        LOGGER.error(error);
        res.status(HTTP.STATUS.CONFLICT).send(error);
    }
}

exports.updateScore = async function(req, res) {
    try {
        let players = await Match.findOneAndUpdate(
            { name: req.params.name },
            { $inc: { "players.$[elem].score": req.body.score } },
            { new: true, arrayFilters: [{ "elem.name": req.body.player }] }
        ).select("players -_id");

        res.status(HTTP.STATUS.OK).json(players);
    } catch (error) {
        LOGGER.info(error);
        res.status(HTTP.STATUS.CONFLICT).send(error);
    }
}

exports.addGame = async function(req, res) {
    try {
        await Match.findOneAndUpdate(
            { name: req.params.name },
            { $push: { games: req.body.game } }
        );
        let matchManager = new MatchManager().getInstance();
        matchManager.addAnswer(req.params.name, req.body.game);
        res.status(HTTP.STATUS.OK).end();
    } catch (error) {
        LOGGER.info(error);
        res.status(HTTP.STATUS.CONFLICT).send(error);
    }
}

exports.getMatch = async function(req, res) {
    try{
        const match = await Match.findOne({ name: req.params.name });
        res.status(HTTP.STATUS.OK).json(match);
    } catch (error) {
        LOGGER.info(error);
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
}

exports.getUnstartedMatch = async function(req, res) {
    try{
        let matches = await Match.find({ status: "Unstarted" });
        res.status(HTTP.STATUS.OK).send(matches);
    } catch (error) {
        LOGGER.info(error);
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
}

exports.updateStatus = async function(req, res) {
    try {
        await Match.findOneAndUpdate(
            { name: req.params.name },
            { status: req.body.status }
        );

        res.status(HTTP.STATUS.OK).end();
    } catch (error) {
        LOGGER.info(error);
        res.status(HTTP.STATUS.CONFLICT).send(error);
    }
}

// Socket
exports.countPlayersInMatch = async function(name) {
    let match = await Match.findOne({ name: name }).select("players -_id");
    if (match.players.length >= 4) {
        throw new Error("Too many players");
    }
}

exports.startStatus = async function(matchId) {
    try {
        await Match.findOneAndUpdate(
            { name: matchId },
            { status: "Started" }
        );
    } catch (error) {
        LOGGER.info(error);
        throw new Exception("La partie ne peux être commencée");
    }
}

// Create a free for all match
exports.createFFAMatch = async function(username) {
    try {         
        let name = username + Timestamp.currentDate();
        let match = new Match({"name": name, "type": "FreeForAll", "players": { "name": username }});
        await match.save();
        return match;
    } catch (error) {
        LOGGER.info(error);
    }
}