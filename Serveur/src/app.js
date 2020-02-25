var express = require('express');
var app = express();
require('./db/db')

var usernamesMap = new Map();
var usernames = new Set();

app.use(express.json({limit: "50mb"}));

var usersRouter = require('./player/player.route.js');
app.use(usersRouter);

module.exports = app;
