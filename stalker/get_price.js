/**
 * This module exports a function that grabs bitcoin price at the time it is run
 */

var request = require('request');

const url = 'https://api.bitcoinaverage.com/ticker/global/USD/';

module.exports = function getPrice(time) {
  return new Promise((resolve, reject) => {
    request(url, (err, res, body) => {
      if (err) reject(err);
      body = JSON.parse(body);
      body._24h_avg = body['24h_avg'];
      delete body['24h_avg'];
      resolve(JSON.stringify(body));
    });
  });
}
