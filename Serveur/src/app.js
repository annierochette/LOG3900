var express = require('express');
var app = express();
require('./db/db')

app.use(express.json({limit: "50mb"}));

let usersRouter = require("./player/player.route");
let generalStatsRouter = require("./player/general.stats.route");
let gameRouter = require("./game/game.route");
let quickdrawRouter = require("./game/quickdraw.route");
let matchRouter = require("./match/match.route");

app.use(usersRouter);
app.use(generalStatsRouter);
app.use(quickdrawRouter);
app.use(gameRouter);
app.use(matchRouter);

module.exports = app;
