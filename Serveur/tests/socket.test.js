const io = require('socket.io-client');
const http = require('http');
const ioBack = require('../src/socket');
const app = require("../src/app");
const CHAT = require("../common/constants/chat");

let socket;
let httpServer;
let httpServerAddr;
let ioServer;

const username = "Mario";
const generalChannel = "General";
const personnalChannel = "Personnal";
const msgBody = "Ceci est un message.";

describe("Socket IO events", () => {
  beforeAll((done) => {
    httpServer = http.createServer(app);
    httpServerAddr = httpServer.listen().address();
    ioServer = new ioBack(httpServer);
    done();
  });

  afterAll((done) => {
    ioServer.close();
    httpServer.close();
    done();
  });
    
  beforeEach((done) => {
    socket = io.connect(`http://[${httpServerAddr.address}]:${httpServerAddr.port}`, {
      'reconnection delay': 0,
      'reopen delay': 0,
      'force new connection': true,
      transports: ['websocket'],
    });

    socket.on("connect", () => {
      done();
    });
  });
    
  afterEach((done) => {
    if (socket.connected) {
      socket.disconnect();
    }
    done();
  });
    
    
  it("should send message with timestamp to the general channel", (done) => {
    socket.on(CHAT.EVENTS.MESSAGE, (msg)=> {
      expect(msg.username).toEqual(username);
      expect(msg.channel).toEqual(generalChannel);
      expect(msg.message).toEqual(msgBody);
      expect(msg.timestamp).toBeDefined();
      done();
    });

    socket.emit(CHAT.EVENTS.MESSAGE, username, generalChannel, msgBody);
  });

  it("should join a channel", (done) => {
    socket.on(CHAT.EVENTS.MESSAGE, (msg) => {
      expect(msg.username).toEqual(username);
      expect(msg.channel).toEqual(personnalChannel);
      expect(msg.message).toEqual(msgBody);
      expect(msg.timestamp).toBeDefined();
      done();
    });

    socket.emit(CHAT.EVENTS.JOIN_CHANNEL, username, personnalChannel);
    socket.emit(CHAT.EVENTS.MESSAGE, username, personnalChannel, msgBody);
  });

  it("should receive messages from joined channels only", (done) => {
    socket.on(CHAT.EVENTS.MESSAGE, (msg) => {
        expect(msg.username).toEqual(username);
        expect(msg.channel).toEqual(generalChannel);
        expect(msg.message).toEqual(msgBody);
        expect(msg.timestamp).toBeDefined();
        done();
      });
  
    socket.emit(CHAT.EVENTS.MESSAGE, username, personnalChannel, "Ceci est faux.");
    setTimeout( () => { socket.emit(CHAT.EVENTS.MESSAGE, username, generalChannel, msgBody); }, 50); 
  });

  it("should leave a joined channel", () => {
    socket.on(CHAT.EVENTS.MESSAGE, (msg) => {
        expect(msg.username).toEqual(username);
        expect(msg.channel).toEqual(generalChannel);
        expect(msg.message).toEqual(msgBody);
        expect(msg.timestamp).toBeDefined();
        done();
      });
  
    socket.emit(CHAT.EVENTS.JOIN_CHANNEL, username, personnalChannel);
    socket.emit(CHAT.EVENTS.LEAVE_CHANNEL, username, personnalChannel);  
    socket.emit(CHAT.EVENTS.MESSAGE, username, personnalChannel, "Ceci est faux.");
    setTimeout( () => { socket.emit(CHAT.EVENTS.MESSAGE, username, generalChannel, msgBody); }, 50);
  });

});
