/**
 *
 */
var gcloud = require('gcloud')();

var auth = { projectId: 'guap-1378' };

var bigquery = gcloud.bigquery(auth);

var dataset = bigquery.dataset('bitcoin');

var table = dataset.table('usd');

module.exports =  function writeToBQ() {
  return new Promise((resolve, reject) => {
    table.import('price.json', { format: 'JSON' }, (err, job, apiResponse) => {
      if (err) reject(err);
      console.log(apiResponse);
      resolve();
    });
  });
}
