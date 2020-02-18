const mongoose = require('mongoose')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')

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
    pseudo: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    password: {
        type: String,
        required: true,
        minLength: 7
    },
    token: {
        type: String
    }
})

playerSchema.pre('save', async function (next) {
    // Hash the password before saving the player model
    const player = this
    if (player.isModified('password')) {
        player.password = await bcrypt.hash(player.password, 8)
    }
    next()
})

playerSchema.methods.generateAuthToken = async function() {
    // Generate an auth token for the player
    const player = this
    const token = jwt.sign({pseudo: player.pseudo}, process.env.JWT_KEY)
    player.token = token;
    await player.save()
    return token;
}

playerSchema.statics.findByCredentials = async (pseudo, password) => {
    // Search for a player by pseudo and password.
    const player = await Player.findOne({pseudo});
    if (!player) {
        console.log("pseudo invalid");
        throw new Error({ error: 'Invalid login credentials' })
    }
    const isPasswordMatch = await bcrypt.compare(password, player.password)
    if (!isPasswordMatch) {
        console.log(`password invalid`);
        throw new Error({ error: 'Invalid login credentials' })
    }
    
    return player
}

playerSchema.methods.removeToken = async (playerPseudo) => {
    await Player.findOneAndUpdate(
        { pseudo: playerPseudo },
        { $unset: { token: ""} });
};

const Player = mongoose.model('Player', playerSchema)

module.exports = Player