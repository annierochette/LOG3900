const jwt = require("jsonwebtoken");
const HTTP = require("../../common/constants/http");
const LOGGER = require("../utils/logger");
const MissingAuthorizationError = require("../errors/missing.authorization");

const authentification = async function(req, res, next) {
    try {
    const AUTHORIZATION = req.header("Authorization");

    if (!AUTHORIZATION) {
        throw new MissingAuthorizationError();
    }

    const token = AUTHORIZATION.replace('Bearer ', '');
    const data = jwt.verify(token, process.env.JWT_KEY);

    next();
    } catch (error) {
        LOGGER.error(error);
        res.status(HTTP.STATUS.UNAUTHORIZED).send({ error: error.message });
    }
};

module.exports = authentification;