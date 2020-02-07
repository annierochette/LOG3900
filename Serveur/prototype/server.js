var app = require('express')();
var http = require('http').createServer(app);
var io = require('socket.io')(http);
const path = require('path');

var usernamesMap = new Map();
var usernames = new Set();

app.get('/', function(req, res){
  // console.log(path.resolve(__dirname + '/../client/index.html'))
  // res.sendFile(path.resolve(__dirname + '/index.html'));
});

io.on('connection', function(socket){
  usernamesMap.set(socket.id, "Anonymous");
  console.log("User connected");

  socket.on('chat message', function(msg){
      var message = JSON.parse(msg);
      message.timestamp = Date.now();
      io.emit('chat message', JSON.stringify(message));
  });
  
  socket.on("changeUsername", function(username){
    var nameUsed = usernames.has(username);  
    if (nameUsed) {
        socket.emit("changeUsername", false);
        socket.disconnect();
    }
    else {
        usernamesMap.set(socket.id, username);
        usernames.add(username);
        socket.emit("changeUsername", true);
    }
  });

  io.on("disconnection", function() {
      usernames.delete(usernamesMap.get(socket.id));
      usernamesMap.delete(socket.id);
      console.log("User disconnected.");
  });

});

http.listen(8080, function(){
  console.log('listening on *:8080');
});