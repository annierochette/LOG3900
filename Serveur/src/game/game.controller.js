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

exports.getRandomGame = async function(req, res) {
    try {
        console.log("DSDFSF")
        const filter = req.body.words;
        console.log("Filter " + filter)
        let words = await Game.find({ name: { $nin: filter} }).select("name -_id");
        console.log("Words: " + words)
        let word = words[Math.floor(Math.random() * words.length)].name;
        let game = await Game.findOne({ name: word });
        res.status(HTTP.STATUS.OK).send({ game });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};