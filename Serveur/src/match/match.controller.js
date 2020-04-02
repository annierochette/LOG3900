const HTTP = require("../../common/constants/http");
const ERR = require("../errors/messages/err");
const LOGGER = require("../utils/logger");
const Match = require("./match.model");

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
        LOGGER.info(error);
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
