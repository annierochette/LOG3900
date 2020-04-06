const GeneralStatistics = require("./general.stats.model");
const HTTP = require("../../common/constants/http");

exports.intialize = async function(username) {
    const generalStats = new GeneralStatistics({ username: username });
    await generalStats.save();
}

exports.delete = async function(username) {
    await GeneralStatistics.deleteOne({ username: username });
}

exports.getGeneralStatistics = async function(req, res) {
    try {
        var generalStats = await GeneralStatistics.findOne({ username: req.params.username });
        res.status(HTTP.STATUS.OK).json({ generalStats });
    } catch (error) {
        res.status(HTTP.STATUS.BAD_REQUEST).json(error);
    }
}

exports.updateStatistics = async function(req, res) {
    await GeneralStatistics.findOneAndUpdate(
        { username: req.body.matchResult.winner },
        // TODO: Utiliser MatchResult lorsque défini
        { $inc: { matchWon: 1 } }
    );

    await GeneralStatistics.updateMany(
        { username: {$in: req.body.matchResult.players } },
        { $inc: { matchPlayed: 1 } }    
    ); 

        res.status(HTTP.STATUS.OK).end();
}