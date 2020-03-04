const CustomError = require("./custom");
const ERR = require("../errors/messages/err");

class UnauthorizedOperationError extends CustomError {
    constructor() {
        super(ERR.MSG.UNAUTHORIZED_OPERATION);
      };
};

module.exports = UnauthorizedOperationError;