var winston = require("winston");

// instantiate a new Winston Logger with the settings defined above
var logger = winston.createLogger({
  transports: [
    new winston.transports.Console(process.env.LOGGER_OPTIONS)
  ],
  exitOnError: false, // do not exit on handled exceptions
});

module.exports = logger;