const app = require("./app");
const PORT = process.env.PORT || 5001;
const morgan = require("morgan");
const HTTP = require("../common/constants/http");
const quickdraw = require("./db/quickdraw");

const http = require('http').createServer(app);

require("./socket")(http);

app.use(morgan("combined", {
  skip: function (req, res) { return res.statusCode < HTTP.STATUS.BAD_REQUEST }
}));

http.listen(PORT, () => {
  console.log('listening on *:', PORT);
  const f = async function(category, amount) {
    const test = await quickdraw.downloadCategory("teapot", 2);
    console.log(test);
  }
  
  const s = async () => {
    let a = await quickdraw.downloadRandomDrawings(5,3);
    console.log(a)
  };
  
});