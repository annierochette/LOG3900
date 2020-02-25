const mongoose = require('mongoose')

mongoose.connect("mongodb+srv://admin:equipe109@cluster0-nmfur.mongodb.net/test?retryWrites=true&w=majority", {
    useNewUrlParser: true,
    useCreateIndex: true,
    useUnifiedTopology: true,
    useFindAndModify: false
});

console.log("Database connected");