const mongoose = require('mongoose');

const matchSchema = mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    games: [{
            type: String,
            required: true
    }],
    turns: {
        type: Number,
        default: 3
    },
    players: {
        type: [{
            name: {   
                type: String,
                required: true,
                trim: true
            },
            score: {
                type: Number,
                default: 0
            },
        }],
    },
    status: {
        type: String,
        enum: ["Unstarted", "Started", "Completed"],
        required: true,
        default: "Unstarted"
    },
    type: {
        type: String,
        enum: ["FreeForAll", "SoloSprint"],
        required: true
    },
});

const Match = mongoose.model("Match", matchSchema);

module.exports = Match;