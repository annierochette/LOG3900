const SocketIo = require('socket.io');
const SOCKET = require("../common/constants/socket");
const MatchManager = require("./match/match.manager");
const Filter = require("bad-words");
const messageController = require("./chat/message.controller");
const playerController = require("./player/player.controller");
const Timestamp = require("./utils/timestamp");

const frenchBadwordsList = require('french-badwords-list');
const GENERAL = "General";
var playersInChannel = new Map();

module.exports = function(http) {
    var io = SocketIo.listen(http);
    var matchManager = new MatchManager(io).getInstance();
    var filter = new Filter();
    filter.addWords(...frenchBadwordsList.array);

    io.on(SOCKET.CHAT.CONNECTION, function(socket){
      messageController.lastPage(socket.id, GENERAL);
      
      socket.join(GENERAL);
      console.log("Users connected: " + io.engine.clientsCount);
      // console.log("User connected" + dateString);
      // console.log("ScoketID: " + socket.id);
    
      socket.on(SOCKET.CHAT.MESSAGE, (username, channel, message) => {
          console.log("Message received");

          let filteredMessage = filter.clean(message);
          let timestamp = Timestamp.currentDate();
          messageController.save(filteredMessage, username, channel, timestamp);

          let  msg = {"message": filteredMessage, "username": username, "timestamp": Timestamp.chatString(timestamp), "channel": channel}

          io.to(channel).emit(SOCKET.CHAT.MESSAGE, msg);
      });
    
    socket.on(SOCKET.CHAT.JOIN_CHANNEL, (username, channel) => {
        socket.join(channel);

        if (playersInChannel.has(channel)) {
          let players = playersInChannel.get(channel);
          players.add(username);
          playersInChannel.set(channel, players);
        } else {
          playersInChannel.set(channel, new Set([username]));
        }
          
        messageController.lastPage(socket.id, channel);
        let timestamp = Timestamp.currentDate();
        let  msg = { "message": username + " a rejoint la conversation.", "username": username, "timestamp": Timestamp.chatString(timestamp), "channel": channel };
        socket.to(channel).broadcast.emit(SOCKET.CHAT.MESSAGE, msg);
      });

      socket.on(SOCKET.CHAT.LEAVE_CHANNEL, (username, channel) => {
        socket.leave(channel);
        let players = playersInChannel.get(channel);
        players.delete(username);

        if (players.size == 0) {
          playersInChannel.delete(channel);
        } else {
          playersInChannel.set(channel, players);
        }

        let timestamp = Timestamp.currentDate();    
        let  msg = { "message": username + " a quitté la conversation.", "username": username, "timestamp": Timestamp.chatString(timestamp), "channel": channel };
        io.to(channel).emit(SOCKET.CHAT.MESSAGE, msg);
      });

      socket.on(SOCKET.CHAT.CHANNELS, () => {
        socket.emit(SOCKET.CHAT.CHANNELS, playersInChannel.keys());
      });

      socket.on(SOCKET.CHAT.HISTORY, async (channel) => {
        let docs = await messageController.previousPage(socket.id);
        socket.to(channel).emit(SOCKET.CHAT.HISTORY, docs);
      });

      socket.on(SOCKET.CHAT.DISCONNECTION, async (player) => {
        socket.disconnect();
        console.log("User disconnected");
        await playerController.deleteToken();
      });

      // Draft
      socket.on(SOCKET.DRAFT.STROKE_DRAWING, (channel, points) => {
        console.log("StrokeDrawing")
        io.emit(SOCKET.DRAFT.STROKE_DRAWING, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_COLLECTED, (channel, points) => {
        //console.log("Stroke: " + stroke);
        io.emit(SOCKET.DRAFT.STROKE_COLLECTED, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_ERASING, (channel, points) => {
        console.log("StrokeErasing")
        io.emit(SOCKET.DRAFT.STROKE_ERASING, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_SEGMENT_ERASING, (channel, points) => {
        console.log("StrokeErasing")
        io.emit(SOCKET.DRAFT.STROKE_SEGMENT_ERASING, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_COLOR, (channel, color) => {
        console.log("Color change")
        io.emit(SOCKET.DRAFT.STROKE_COLOR, color);
      });

      socket.on(SOCKET.DRAFT.STROKE_SIZE, (channel, size) => {
        console.log("Size change")
        io.emit(SOCKET.DRAFT.STROKE_SIZE, size);
      });

      socket.on(SOCKET.DRAFT.STROKE_TIP, (channel, tip) => {
        console.log("Tip change")
        io.emit(SOCKET.DRAFT.STROKE_TIP, tip);
      });

      socket.on(SOCKET.DRAFT.STROKE_TOOL, (channel, tool) => {
        io.emit(SOCKET.DRAFT.STROKE_TOOL, tool);
      });

      // Match
      socket.on(SOCKET.MATCH.JOIN_MATCH, (channel, nbPlayers) => {
        console.log("joining game")
        let  nbPlay = { "nbPlayers": "1" };
        io.emit(SOCKET.MATCH.JOIN_MATCH, nbPlay);
      });

      socket.on(SOCKET.MATCH.ANSWER, (matchId, answer) => {
        socket.emit(SOCKET.MATCH.ANSWER, matchManager.validateAnswer(matchId, answer));
      });

      socket.on(SOCKET.MATCH.START_ROUND, (matchId) => {
        matchManager.start(matchId, 90);
        io.to(matchId).emit(SOCKET.EMIT.START_ROUND, "Round started");
      });

      socket.on(SOCKET.MATCH.NEXT_ROUND, async (matchId) => {
        let round = await matchManager.nextRound(matchId);
        io.to(matchId).emit(SOCKET.MATCH.NEXT_ROUND, round);
      });

      socket.on(SOCKET.MATCH.START_MATCH, async (players) => {
        matchManager.addMatch(matchId, players);
        let round = await matchManager.nextRound(matchId);
        io.to(matchId).emit(SOCKET.MATCH.NEXT_ROUND, round);
        matchManager.start(matchId, 90);
      });
    
    });
}

