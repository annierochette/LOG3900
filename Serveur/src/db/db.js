const mongoose = require('mongoose');

exports.connect = function(url, dbname) {
    mongoose.connect(url, {
        dbName: dbname || "temporary",
        useNewUrlParser: true,
        useCreateIndex: true,
        useUnifiedTopology: true,
        useFindAndModify: false
    });

    console.log("Database connected on " + process.env.DBNAME);
}

exports.disconnect = function() {
    mongoose.disconnect();
}
