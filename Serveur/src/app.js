var express = require('express');
var app = express();
require('./db/db')

app.use(express.json({limit: "50mb"}));

var usersRouter = require("./player/player.route");
var generalStatsRouter = require("./player/general.stats.route");
app.use(usersRouter);
app.use(generalStatsRouter);

module.exports = app;
