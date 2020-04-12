const app = require("./app");
const PORT = process.env.PORT || 5001;
const morgan = require("morgan");
const HTTP = require("../common/constants/http");
const mongoose = require('./db/db');

const http = require('http').createServer(app);
require("./socket")(http);
mongoose.connect(process.env.MONGODB_URL, "svg");

app.use(morgan("combined", {
  skip: function (req, res) { return res.statusCode < HTTP.STATUS.BAD_REQUEST }
}));

http.listen(PORT, () => {
  console.log('listening on *:', PORT);
});