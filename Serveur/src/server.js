const app = require("./app");
const PORT = process.env.PORT || 5001;
const morgan = require("morgan");
const HTTP = require("../common/constants/http");

var http = require('http').createServer(app);
var io = require('socket.io')(http);

app.use(morgan("combined", {
  skip: function (req, res) { return res.statusCode < HTTP.STATUS.BAD_REQUEST }
}));

io.attach(http);
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
      socket.disconnect();
      console.log("User disconnected.");
  });

});

http.listen(PORT, () => {
  console.log('listening on *:', PORT);
});