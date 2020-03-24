const mongoose = require('mongoose');

const gameSchema = mongoose.Schema({
    name: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    clues: [{
        type: String,
        required: true
    }],
    data: [{
        type: Buffer,
        required: true
    }]
});

const Game = mongoose.model("Game", gameSchema);

module.exports = Game;