const jwt = require("jsonwebtoken");
const HTTP = require("../constants/http");

const authentification = async function(req, res, next) {
    try {
    const AUTHORIZATION = req.header("Authorization");

    if (!AUTHORIZATION) {
        console.log("No authorization has been received in the request header.");
        throw new Error();
    }

    const token = AUTHORIZATION.replace('Bearer ', '');
    const data = jwt.verify(token, process.env.JWT_KEY);

    next();
    } catch (error) {
        res.status(HTTP.STATUS.UNAUTHORIZED).send({ error: 'Not authorized to access this resource' })
    }
};

module.exports = authentification;