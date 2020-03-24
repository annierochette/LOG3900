const SocketIo = require('socket.io');
const SOCKET = require("../common/constants/socket");

module.exports = function(http) {
    var io = SocketIo.listen(http);

    io.on(SOCKET.CHAT.CONNECTION, function(socket){
      socket.join("General");
      console.log("Users connected: " + io.engine.clientsCount);
    
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

          io.to(channel).emit('chat message', msg);
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
        socket.to(channel).broadcast.emit("chat message", msg);
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

      // Drawing
      socket.on(SOCKET.CHAT.STROKE, (channel, points) => {
        socket.to(channel).broadcast.emit(SOCKET.CHAT.STROKE, points);
      });

      socket.on(SOCKET.CHAT.DRAFTSMAN_DIMENSION, (channel, width, height) => {
        console.log("Dimension received");
        let dimension = {
          "width": width,
          "height": height
        };
        socket.to(channel).broadcast.emit(SOCKET.CHAT.DRAFTSMAN_DIMENSION, dimension);
      });

      socket.on(SOCKET.CHAT.DRAWING_ATTRIBUTES, (channel, drawingAttributes) => {
        socket.to(channel).broadcast.emit(SOCKET.CHAT.MODIFY_PROPERTY, drawingAttributes);
      });
        
      socket.on(SOCKET.CHAT.DISCONNECTION, () => {
          socket.disconnect();
          console.log("User disconnected.");
      });

      // Draft
      socket.on(SOCKET.DRAFT.STROKE_DRAWING, (channel, points) => {
        io.emit(SOCKET.CHAT.STROKE_DRAWING, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_COLLECTED, (channel, points) => {
        //console.log("Stroke: " + stroke);
        io.emit(SOCKET.CHAT.STROKE_COLLECTED, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_ERASING, (channel, points) => {
        console.log("StrokeErasing")
        io.emit(SOCKET.CHAT.STROKE_ERASING, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_SEGMENT_ERASING, (channel, points) => {
        console.log("StrokeErasing")
        io.emit(SOCKET.CHAT.STROKE_SEGMENT_ERASING, points);
      });

      socket.on(SOCKET.DRAFT.STROKE_COLOR, (channel, color) => {
        io.emit(SOCKET.CHAT.STROKE_COLOR, color);
      });

      socket.on(SOCKET.DRAFT.STROKE_SIZE, (channel, size) => {
        io.emit(SOCKET.CHAT.STROKE_SIZE, size);
      });

      socket.on(SOCKET.DRAFT.STROKE_TIP, (channel, tip) => {
        io.emit(SOCKET.CHAT.STROKE_TIP, tip);
      });

      socket.on(SOCKET.DRAFT.STROKE_TOOL, (channel, tool) => {
        io.emit(SOCKET.CHAT.STROKE_TOOL, tool);
      });
    
    });
}
