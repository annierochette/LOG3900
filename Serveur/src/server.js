const app = require("./app");
const PORT = process.env.PORT || 5001;
const morgan = require("morgan");
const HTTP = require("../common/constants/http");

app.use(morgan("combined", {
  skip: function (req, res) { return res.statusCode < HTTP.STATUS.BAD_REQUEST }
}));

app.listen(PORT, () => {
  console.log('listening on *:', PORT);
});