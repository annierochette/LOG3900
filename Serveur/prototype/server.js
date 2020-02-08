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

  socket.on('chat message', (username, message) => {

      var currentDate = new Date();
      // var date = currentDate.getDate();
      // var month = currentDate.getMonth();
      // var year = currentDate.getFullYear();
      var hours = currentDate.getHours();
      var minutes = currentDate.getMinutes();
      var seconds = currentDate.getSeconds();

      var dateString = " à " + hours + ":" + minutes + ":" + seconds;

      let  msg = {"message": message, "username": username, "timestamp": dateString}
      console.log(username + ":" + message)
      io.emit('chat message', msg);
  });
  
  socket.on("changeUsername", function(username){
    var nameUsed = usernames.has(username);  
    if (nameUsed) {
        socket.emit("changeUsername", false);
    }
    else {
        usernamesMap.set(socket.id, username);
        usernames.add(username);
        socket.emit("changeUsername", true);
        console.log(username + "has joined the chat")
    }
  });

  socket.on("disconnection", function() {
      usernames.delete(usernamesMap.get(socket.id));
      usernamesMap.delete(socket.id);
      console.log("User disconnected.");
  });

});

http.listen(5050, function(){
  console.log('listening on *:5050');
});