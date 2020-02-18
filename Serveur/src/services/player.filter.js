const jwt = require("jsonwebtoken");

const playerFilter = async function(req, res, next) {
    // Use after authentification, so token is valid
    const data = jwt.decode(req.header("Authorization").replace('Bearer ', ''), process.env.JWT_KEY);
    const requestor = data.username;

    req.params.filter = (requestor === req.params.username)
        ? ""
        : "username avatar -_id";

    next();
};

module.exports = playerFilter;