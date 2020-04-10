const HTTP = require("../../common/constants/http");
var crypto = require("crypto");
const nameGenerator = async function(req, res, next) {
    var name = crypto.randomBytes(20).toString('hex');
    req.body.name = name;
    next();
} 

module.exports = nameGenerator;