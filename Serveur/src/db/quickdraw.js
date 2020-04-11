const HTTP = require("../../common/constants/http");
const https = require("https");
const ndjson = require("ndjson");
const categories = require("../../common/constants/categories");

const PROBABILITY = 0.01;

exports.downloadCategory = function (category, amount) {
    var url = 'https://storage.googleapis.com/quickdraw_dataset/full/raw/';
    url += encodeURIComponent(category) + '.ndjson';


    return new Promise((resolve, reject) => {
      https.get(url, (res) => {
        var { statusCode } = res;
        if (statusCode !== HTTP.STATUS.OK) {
          throw new Error(`Request Failed.\n Status Code: ${statusCode}`);
        }

        res.setEncoding('utf8');
        var drawings = [];
        res
          .pipe(ndjson.parse())
          .on('data', function (obj) {
              if (drawings.length < amount) {
                let keepDrawing = Math.random() <= PROBABILITY;

                if (keepDrawing) {
                  drawings.push(obj);
                }
              } 

              if (drawings.length == amount){
                resolve(drawings);
                res.pause();
              }
          })
          .on('end', () => {
            resolve(drawings);
          })
          .on('close', () => {
            resolve(drawings);
          });
      }).on('error', (e) => {
        console.log("ERR");
      });
    });
};

exports.downloadRandomDrawings = async function (quantity, amount) {
  shuffle(categories);
  const drawings = [];
    for (let i = 0; i < quantity; i++) {
      let category = categories[i];
      const drawing = await this.downloadCategory(category, amount);
      drawings.push(drawing);
    }

    return drawings;
};

function shuffle(array) {
  var currentIndex = array.length, temporaryValue, randomIndex;

  // While there remain elements to shuffle...
  while (0 !== currentIndex) {

    // Pick a remaining element...
    randomIndex = Math.floor(Math.random() * currentIndex);
    currentIndex -= 1;

    // And swap it with the current element.
    temporaryValue = array[currentIndex];
    array[currentIndex] = array[randomIndex];
    array[randomIndex] = temporaryValue;
  }

  return array;
};