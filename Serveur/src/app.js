var express = require('express');
var app = express();

app.use(express.json({limit: "50mb"}));

let usersRouter = require("./player/player.route");
let generalStatsRouter = require("./player/general.stats.route");
let gameRouter = require("./game/game.route");
let matchRouter = require("./match/match.route");
let quickdrawRouter = require("./game/quickdraw.route");


app.use(usersRouter);
app.use(generalStatsRouter);
app.use(gameRouter);
app.use(matchRouter);
app.use(quickdrawRouter);



module.exports = app;
