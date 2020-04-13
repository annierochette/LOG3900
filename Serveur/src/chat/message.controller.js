const Message = require("./message.model");
const PAGE_SIZE = 25;

var pageKeeper = new Map();

exports.previousPage = async function(player, channel) {
    console.log(player + " " + channel);

    let infos = pageKeeper.get(player).get(channel);

    if (!infos || infos.offset < 0) { return; }

    let results = await Message.paginate({}, { offset: infos.offset, limit: infos.documents });
    let newInfos = { "offset": infos.offset - PAGE_SIZE, "documents": PAGE_SIZE };
    pageKeeper.get(player).set(channel, newInfos);

    return results.docs;
}

exports.lastPage = async function(player, channel) {
    let collection = await Message.paginate({}, { limit: 0 });
    let offset = Math.floor(collection.total / PAGE_SIZE) * PAGE_SIZE;
    let remaining = collection.total - offset;
    let channelInfos =  new Map()
    if (remaining == 0) {
        offset -= offset - PAGE_SIZE;
        channelInfos.set(channel, {"offset": offset, "documents": PAGE_SIZE});
    } else {
        channelInfos.set(channel, {"offset": offset, "documents": remaining});
    }
    pageKeeper.set(player, channelInfos);

    console.log("LAST "+ player + " " + channel);
}

exports.save = async function(message, username, channel, timestamp) {
    const doc = new Message({ "message": message, "username": username, "channel": channel, "timestamp": timestamp });
    await doc.save();
}