const  mongoose  = require("mongoose");
const mongoosePaginate = require('mongoose-paginate-v2');
const  Schema  =  mongoose.Schema;

const  messageSchema  =  new Schema({
    channel: {
        type: String,
        required: true
    },
    message: {
        type: String,
        required: true
    },
    username: {
        type: String,
        required: true
    },
    timestamp: {
        type: Number
    }
});

messageSchema.plugin(mongoosePaginate);

let  Message  =  mongoose.model("Message", messageSchema);
module.exports  =  Message;
