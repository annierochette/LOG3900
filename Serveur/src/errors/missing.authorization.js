const CustomError = require("./custom");
const ERR = require("./messages/err");

class MissingAuthorizationError extends CustomError {
    constructor() {
        super(ERR.MSG.MISSING_AUTHORIZATION);
      };
};

module.exports = MissingAuthorizationError;