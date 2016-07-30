/**
 *
 */
var gcloud = require('gcloud')();

var auth = { projectId: 'guap-1378' };

if (process.env.GCP_AUTH_JSON) {
 auth.keyFilename = process.env.GCP_AUTH_JSON;
}

var bigquery = gcloud.bigquery(auth);

var dataset = bigquery.dataset('bitcoin');

dataset.createTable('usd', {
  schema: '_24h_avg:float,ask:float,bid:float,last:float,timestamp:timestamp,volume_btc:float,volume_percent:float'
}, (err, table) => {
  if (err) console.error(err);
});
