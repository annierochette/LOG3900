const Message = require("./message.model");
const PAGE_SIZE = 25;

var pageKeeper = new Map();

exports.previousPage = async function(player, channel) {
    let key = { "player": player, "channel": channel };
    let infos = pageKeeper.get(key);

    if (infos.offset < 0) { return; }

    let results = await Message.paginate({}, { offset: infos.offset, limit: infos.documents });
    let newInfos = { "offset": offset - PAGE_SIZE, "documents": PAGE_SIZE };
    pageKeeper.set(key, newInfos);

    return results.docs;
}

exports.lastPage = async function(player, channel) {
    let collection = await Message.paginate({}, { limit: 0 });
    let offset = Math.floor(collection.total / PAGE_SIZE) * PAGE_SIZE;
    let remaining = collection.total - offset;
    let key = { "player": player, "channel": channel };
    if (remaining == 0) {
        offset -= offset - PAGE_SIZE;
        pageKeeper.set(key, {"offset": offset, "documents": PAGE_SIZE});
    } else {
        pageKeeper.set(key, {"offset": offset, "documents": remaining});
    }
}

exports.save = async function(message, username, channel, timestamp) {
    const doc = new Message({ "message": message, "username": username, "channel": channel, "timestamp": timestamp });
    await doc.save();
}