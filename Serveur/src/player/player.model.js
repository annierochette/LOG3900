const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const PASSWORD_MIN_LENGTH = 7;

const playerSchema = mongoose.Schema({
    firstName: {
        type: String,
        required: true,
        trim: true
    },
    lastName: {
        type: String,
        required: true,
        trim: true
    },
    username: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    password: {
        type: String,
        required: true,
    },
    avatar: {
        type: String,
        required: false
    },
    token: {
        type: String,
        required: false
    }
});

playerSchema.pre('save', async function (next) {
    // Hash the password before saving the player model
    const player = this
    
    if (player.isModified("password")) {
        
        if (player.password.length < PASSWORD_MIN_LENGTH) {
            throw new Error({ error: "Password must have at least " + PASSWORD_MIN_LENGTH + " characters" });
        }
        
        player.password = await bcrypt.hash(player.password, 8);
    }

    next();
})

playerSchema.methods.generateAuthToken = async function() {
    // Generate an auth token for the player
    const player = this;
    const token = jwt.sign({username: player.username}, process.env.JWT_KEY);
    player.token = token;
    await player.save();
    return token;
}

playerSchema.statics.findByCredentials = async function (username, password) {
    // Search for a player by username and password.
    const player = await Player.findOne({ username });
    if (!player) {
        LOGGER.info("username invalid");
        throw new Error({ error: 'Invalid login credentials' })
    }
    const isPasswordMatch = await bcrypt.compare(password, player.password)
    if (!isPasswordMatch) {
        LOGGER.info(`password invalid`);
        throw new Error({ error: 'Invalid login credentials' })
    }
    
    return player
}

playerSchema.statics.removeToken = async function (playerName) {
    await Player.findOneAndUpdate(
        { username: playerName },
        { $unset: { token: ""} }
    );
};

playerSchema.statics.changeAvatar = async function (req) {
    await Player.findOneAndUpdate(
        { username: req.params.username },
        { avatar: req.body.avatar }
    );
};

const Player = mongoose.model('Player', playerSchema);

module.exports = Player