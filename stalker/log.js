/**
 *
 */
var fs = require('fs');

module.exports = {

  error: e => {
    console.log(e);
    fs.appendFile('error.log', JSON.stringify(e));
  }

};
