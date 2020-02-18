const jwt = require("jsonwebtoken");

const playerFilter = async function(req, res, next) {
    // Use after authentification, so token is valid
    const data = jwt.decode(req.header("Authorization").replace('Bearer ', ''), process.env.JWT_KEY);
    const requestor = data.pseudo;

    req.params.fields = (requestor === req.params.pseudo)
        ? ""
        : "pseudo -_id";

    next();
};

module.exports = playerFilter;