const jwt = require('jsonwebtoken')
const Player = require('../player/player.model.js')

const authentification = async(req, res, next) => {
    
    try {
    const AUTHORIZATION = req.header("Authorization");

    if (!AUTHORIZATION) {
        console.log("No authorization has been received in the request header.");
        throw new Error();
    }

    const token = AUTHORIZATION.replace('Bearer ', '');
    const data = jwt.verify(token, process.env.JWT_KEY);

    req.pseudo = data.pseudo;
    next()
    } catch (error) {
        res.status(401).send({ error: 'Not authorized to access this resource' })
    }
}

module.exports = authentification