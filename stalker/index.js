/**
 *
 */
var fs = require('fs');
var log = require('./log');
var getPrice = require('./get_price');
var writeToBQ = require('./write_to_bq');

getPrice()
  .then(price => {
    return new Promise((resolve, reject) => {
      fs.writeFile('price.json', price, err => {
        if (err) reject(err);
        resolve();
      })
    });
  })
  .then(writeToBQ)
  .catch(log.error);
