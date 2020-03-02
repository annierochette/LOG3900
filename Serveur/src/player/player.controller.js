const Player = require("./player.model");
const HTTP = require("../../common/constants/http");
const ERR = require("../errors/messages/err");
const jwt = require("jsonwebtoken");
const LOGGER = require("../utils/logger");

const GeneralStatsController = require("./general.stats.controller");

exports.createPlayer = async function(req, res) {
    // Create a new player
    try {
        const player = new Player(req.body)
        player.token = await player.generateAuthToken();
        await player.save();
        await GeneralStatsController.intialize(player.username);
        res.status(HTTP.STATUS.CREATED).json({ player });
    } catch (error) {
        console.log("Error ", error)
        res.status(HTTP.STATUS.BAD_REQUEST).send(error)
    }
};

exports.deletePlayer = async function(req, res) {
    try {
        await Player.deleteOne({ username: req.params.username });
        await GeneralStatsController.delete(req.params.username);
        res.status(HTTP.STATUS.OK).send();
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send();
    }
};

exports.getSinglePlayerInfos = async function(req, res) {
    try {
        var player = await Player.findOne({ username: req.params.username }).select(req.params.filter);
        res.status(HTTP.STATUS.OK).send({ player });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};

exports.changeAvatar = async function(req, res) {
    try {
        await Player.changeAvatar(req);
        res.status(HTTP.STATUS.OK).send();
    } catch (error) {
        console.log(error);
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};

exports.login = async function(req, res) {
    //Login a registered player
    try {
        const player = await Player.findByCredentials(req.body.username, req.body.password);
        
        if (!player) {
            return res.status(HTTP.STATUS.UNAUTHORIZED).send({ error: ERR.MSG.WRONG_CREDENTIALS });
        }

        if (player.token)
        {
            return res.status(HTTP.STATUS.UNAUTHORIZED).send({ error: ERR.MSG.ALREADY_CONNECTED });
        }

        const token = await player.generateAuthToken()
        res.status(HTTP.STATUS.OK).send({ player })
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error)
    }
};

exports.logout = async function(req, res) {
    // Log user out of the application
    try {
        await Player.removeToken(req.params.username);
        res.status(HTTP.STATUS.OK).send("Token removed");
    } catch (error) {
        res.status(HTTP.STATUS.INTERNAL_SERVER_ERROR).send(error);
    }
};