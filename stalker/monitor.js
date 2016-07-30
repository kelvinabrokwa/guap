/**
 *
 */
var express = require('express');
var cors = require('cors');
var google = require('googleapis');

var bigquery = google.bigquery('v2');

var app = express();
app.use(cors())

app.get('/monitor/jobs', () => {

});

app.listen(80, () => {
  console.log('Monitor listening on port 80');
})
