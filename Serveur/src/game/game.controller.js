const Game = require("./game.model");
const HTTP = require("../../common/constants/http");
const ERR = require("../errors/messages/err");
const jwt = require("jsonwebtoken");
const LOGGER = require("../utils/logger");

exports.createGame = async function(req, res) {
    // Create a new player
    try {
        const game = new Game(req.body)
        await game.save();
        res.status(HTTP.STATUS.CREATED).json({ game });
    } catch (error) {
        res.status(HTTP.STATUS.CONFLICT).send(error)
    }
};

exports.deleteGame = async function(req, res) {
    try {
        console.log("Name: ");
        await Game.deleteOne({ name: req.params.name });
        res.status(HTTP.STATUS.OK).send();
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send();
    }
};

exports.getGame = async function(req, res) {
    try {
        let game = await Game.findOne({ name: req.params.name });
        res.status(HTTP.STATUS.OK).send({ game });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};