var app = require('express')();
var http = require('http').createServer(app);
var io = require('socket.io')(http);
const path = require('path');

app.get('/', function(req, res){
  // console.log(path.resolve(__dirname + '/../client/index.html'))
  // res.sendFile(path.resolve(__dirname + '/../client/index.html'));
});

io.on('connection', function(socket){
  socket.on('chat message', function(msg){
    io.emit('chat message', msg);
  });
});

http.listen(8080, function(){
  console.log('listening on *:8080');
});