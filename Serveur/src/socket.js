const SocketIo = require('socket.io');
const CHAT = require("../common/constants/chat");

module.exports = function(http) {
    var io = SocketIo.listen(http);

    io.on(CHAT.EVENTS.CONNECTION, function(socket){
      socket.join("General");
      console.log("Users connected: " + io.engine.clientsCount);
    
      socket.on(CHAT.EVENTS.MESSAGE, (username, channel, message) => {
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

          io.to(channel).emit('chat message', msg);
      });
    
    socket.on(CHAT.EVENTS.JOIN_CHANNEL, (username, channel) => {
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
        socket.to(channel).broadcast.emit("chat message", msg);
      });

      socket.on(CHAT.EVENTS.LEAVE_CHANNEL, (username, channel) => {
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
        io.to(channel).emit(CHAT.EVENTS.MESSAGE, msg);
      });

      // Drawing
      socket.on(CHAT.EVENTS.STROKE, (channel, points) => {
        socket.to(channel).broadcast.emit(CHAT.EVENTS.STROKE, points);
      });

      socket.on(CHAT.EVENTS.DRAFTSMAN_DIMENSION, (channel, width, height) => {
        console.log("Dimension received");
        let dimension = {
          "width": width,
          "height": height
        };
        socket.to(channel).broadcast.emit(CHAT.EVENTS.DRAFTSMAN_DIMENSION, dimension);
      });

      socket.on(CHAT.EVENTS.DRAWING_ATTRIBUTES, (channel, drawingAttributes) => {
        socket.to(channel).broadcast.emit(CHAT.EVENTS.MODIFY_PROPERTY, drawingAttributes);
      });
        
      socket.on(CHAT.EVENTS.DISCONNECTION, () => {
          socket.disconnect();
          console.log("User disconnected.");
      });

      // Draft
      socket.on(CHAT.EVENTS.STROKE_DRAWING, (channel, points) => {
        io.emit(CHAT.EVENTS.STROKE_DRAWING, points);
      });

      socket.on(CHAT.EVENTS.STROKE_COLLECTED, (channel, points) => {
        //console.log("Stroke: " + stroke);
        io.emit(CHAT.EVENTS.STROKE_COLLECTED, points);
      });

      socket.on(CHAT.EVENTS.STROKE_ERASING, (channel, points) => {
        console.log("StrokeErasing")
        io.emit(CHAT.EVENTS.STROKE_ERASING, points);
      });

      socket.on(CHAT.EVENTS.STROKE_SEGMENT_ERASING, (channel, points) => {
        console.log("StrokeErasing")
        io.emit(CHAT.EVENTS.STROKE_SEGMENT_ERASING, points);
      });

      socket.on(CHAT.EVENTS.STROKE_COLOR, (channel, color) => {
        io.emit(CHAT.EVENTS.STROKE_COLOR, color);
      });

      socket.on(CHAT.EVENTS.STROKE_SIZE, (channel, size) => {
        io.emit(CHAT.EVENTS.STROKE_SIZE, size);
      });

      socket.on(CHAT.EVENTS.STROKE_TIP, (channel, tip) => {
        io.emit(CHAT.EVENTS.STROKE_TIP, tip);
      });

      socket.on(CHAT.EVENTS.STROKE_TOOL, (channel, tool) => {
        io.emit(CHAT.EVENTS.STROKE_TOOL, tool);
      });
      socket.on("joinGame", (channel, nbPlayers) => {
        console.log("joining game")
        let  nbPlay = { "nbPlayers": "1" };
        io.emit("joinGame", nbPlay);
      });
    
    });
}
