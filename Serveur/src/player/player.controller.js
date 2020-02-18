var Player = require("./player.model");
var HTTP = require("../constants/http");

exports.createPlayer = async function(req, res) {
    // Create a new player
    try {
        const player = new Player(req.body)
        await player.save();
        const token = await player.generateAuthToken()
        res.status(HTTP.STATUS.CREATED).send({ player, token })
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error)
    }
};

exports.deletePlayer = async function(req, res) {
    try {
        await Player.deleteOne({pseudo: req.params.pseudo});
        res.status(HTTP.STATUS.OK).send();
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send();
    }
};

exports.getSinglePlayerInfos = async function(req, res) {
    try {
        var player = await Player.findOne({ pseudo: req.params.pseudo }).select(req.params.fields);
        res.status(HTTP.STATUS.OK).send({ player });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};

exports.login = async function(req, res) {
    //Login a registered player
    try {
        const player = await Player.findByCredentials(req.body.pseudo, req.body.password);
        
        if (!player) {
            return res.status(HTTP.STATUS.UNAUTHORIZED).send({error: 'Login failed! Check authentication credentials'})
        }

        if (player.token)
        {
            return res.status(HTTP.STATUS.UNAUTHORIZED).send({error: 'Already connected'})
        }
        const token = await player.generateAuthToken()
        res.status(HTTP.STATUS.OK).send({ player, token })
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error)
    }
};

exports.logout = async function(req, res) {
    // Log user out of the application
    try {
        await Player.removeToken(req.pseudo);
        res.status(HTTP.STATUS.OK).send("Toekn removed");
    } catch (error) {
        res.status(HTTP.STATUS.INTERNAL_SERVER_ERROR).send(error)
    }
};