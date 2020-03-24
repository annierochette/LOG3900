const mongoose = require('mongoose');

const generalStatisticsSchema = mongoose.Schema({
    username: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    matchPlayed: {
        type: Number,
        default: 0
    },
    matchWon: {
        type: Number,
        default: 0
    }
});

const GeneralStatistics = mongoose.model("GeneralStatistics", generalStatisticsSchema);

module.exports = GeneralStatistics;
