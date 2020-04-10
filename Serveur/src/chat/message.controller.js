const Message = require("./message.model");
const PAGE_SIZE = 25;

var pageKeeper = new Map();

exports.previousPage = async function() {
    let results = await Message.paginate({}, { limit: PAGE_SIZE, page: pageKeeper.get(player) });
    return results.docs;
}

exports.lastPage = function(player) {
    let collection = await Message.paginate({}, { limit: 0 });
    let actualPage = collection.total / PAGE_SIZE;
    pageKeeper.set(player, actualPage);
}

exports.save = async function(message, username, channel) {
    const message = new Message(message, username, channel);
    await message.save();
}