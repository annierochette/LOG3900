var express = require('express');
var app = express();
var http = require('http').createServer(app);
require('./db/db')

app.use(express.json({limit: "50mb"}));

var usersRouter = require('./player/player.route.js');
app.use(usersRouter);

module.exports = app;
