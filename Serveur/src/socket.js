const SocketIo = require('socket.io');
const SOCKET = require("../common/constants/socket");
const MatchManager = require("./match/match.manager");

module.exports = function(http) {
    var io = SocketIo.listen(http);
    var matchManager = new MatchManager(io).getInstance();

    io.on(SOCKET.CHAT.CONNECTION, function(socket){
      var currentDate = new Date();
          // var date = currentDate.getDate();
          // var month = currentDate.getMonth();
          // var year = currentDate.getFullYear();
          var hours = currentDate.getHours();
          var minutes = currentDate.getMinutes();
          var seconds = currentDate.getSeconds();
    
          var dateString = " à " + hours + ":" + minutes + ":" + seconds;
      
      socket.join("General");
      console.log("Users connected: " + io.engine.clientsCount);
      console.log("User connected" + dateString);
      console.log("ScoketID: " + socket.id);
    
      socket.on(SOCKET.CHAT.MESSAGE, (username, channel, message) => {
          console.log("Message received")
          var currentDate = new Date();
          // var date = currentDate.getDate();
          // var month = currentDate.getMonth();
          // var year = currentDate.getFullYear();
          var hours = currentDate.getHours();
          var minutes = currentDate.getMinutes();
          var seconds = currentDate.getSeconds();
    
          var dateString = " à " + hours + ":" + minutes + ":" + seconds;
    
          let  msg = {"message": message, "username": username, "timestamp": dateString, "channel": channel}

          io.to(channel).emit(SOCKET.CHAT.MESSAGE, msg);
      });
    
    socket.on(SOCKET.CHAT.JOIN_CHANNEL, (username, channel) => {
        socket.join(channel);
        var currentDate = new Date();
        
        // var date = currentDate.getDate();
        // var month = currentDate.getMonth();
        // var year = currentDate.getFullYear();
        var hours = currentDate.getHours();
        var minutes = currentDate.getMinutes();
        var seconds = currentDate.getSeconds();
    
        var dateString = " à " + hours + ":" + minutes + ":" + seconds;
        let  msg = { "message": username + " a rejoint la conversation.", "username": username, "timestamp": dateString, "channel": channel };
        socket.to(channel).broadcast.emit(SOCKET.CHAT.MESSAGE, msg);
      });

      socket.on(SOCKET.CHAT.LEAVE_CHANNEL, (username, channel) => {
        socket.leave(channel);
        var currentDate = new Date();
        
        // var date = currentDate.getDate();
        // var month = currentDate.getMonth();
        // var year = currentDate.getFullYear();
        var hours = currentDate.getHours();
        var minutes = currentDate.getMinutes();
        var seconds = currentDate.getSeconds();
    
        var dateString = " à " + hours + ":" + minutes + ":" + seconds;
        let  msg = { "message": username + " a quitté la conversation.", "username": username, "timestamp": dateString, "channel": channel };
        io.to(channel).emit(SOCKET.CHAT.MESSAGE, msg);
      });

      socket.on(SOCKET.CHAT.DISCONNECTION, () => {
        socket.disconnect();
        console.log("User disconnected");
      });

      // Draft
      socket.on(SOCKET.DRAFT.STROKE_DRAWING, (channel, points) => {
        console.log(points)
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
        io.emit(SOCKET.DRAFT.STROKE_COLOR, color);
      });

      socket.on(SOCKET.DRAFT.STROKE_SIZE, (channel, size) => {
        io.emit(SOCKET.DRAFT.STROKE_SIZE, size);
      });

      socket.on(SOCKET.DRAFT.STROKE_TIP, (channel, tip) => {
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

      socket.on(SOCKET.MATCH.START, (matchId) => {
        matchManager.start(matchId, 90);
        io.to(matchId).emit(SOCKET.EMIT.START, "Round started");
      });
    
    });
}