const HTTP = require("../../common/constants/http");
const ERR = require("../errors/messages/err");
const quickdraw = require("../db/quickdraw");

const SAMPLE_SIZE = 10;

exports.getRandomDrawings = async function(req, res) {
    try {
        let drawings = await quickdraw.downloadRandomDrawings(SAMPLE_SIZE, 1);
        res.status(HTTP.STATUS.OK).send({ drawings });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};

exports.getCategory = async function(req, res) {
    try {
        let drawings = await quickdraw.downloadCategory(req.params.category, 1);
        res.status(HTTP.STATUS.OK).send({ drawings });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).send(error);
    }
};