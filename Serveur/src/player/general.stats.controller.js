const GeneralStatistics = require("./general.stats.model");
const HTTP = require("../../common/constants/http");

exports.getGeneralStatistics = async function(req, res) {
    try {
        console.log("fdsf");
        var generalStats = await GeneralStatistics.findOne({ username: req.params.username });
        console.log("fdsffdsfsd");
        res.status(HTTP.STATUS.OK).json({ generalStats });
    } catch (error) {
        console.log("fdsffdsfsdfsfsdf");
        res.status(HTTP.STATUS.BAD_REQUEST).json(error);
    }
}


