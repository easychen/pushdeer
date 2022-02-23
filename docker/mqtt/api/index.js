const express = require('express');
const path = require('path');
const fs = require('fs');
const timeout = require('connect-timeout')
const app = express();

var multer = require('multer');
var forms = multer();
const bodyParser = require('body-parser')
app.use(bodyParser.json());
app.use(forms.array()); 
app.use(bodyParser.urlencoded({ extended: true }));

app.get(`/`, (req, res) => {
  res.sendFile(path.join(__dirname, 'index.html'));
});

function haltOnTimedout (req, res, next) {
    if (!req.timedout) next()
}

app.all('/send', timeout('3s'), haltOnTimedout, async (req, res) => {
  
  console.log( process.env , req.body, req.query );
  if( process.env.API_KEY )
  {
    const in_key = req.body.key||req.query.key || "";
    if( in_key != process.env.API_KEY ) return res.status(403).send(`api key error `+ in_key );
  }
  
  const msg_type = (req.body.type || req.query.type) == "bg_url" ? "bg_url"  : "text";
  let msg_content = req.body.content || req.query.content || "";
  if( msg_type== 'bg_url' && msg_content.length < 1 ) res.status(500).send('Bg url cannot be empty');
  if( msg_type== 'bg_url' ) msg_content = msg_content.replace("https://","http://");
  const msg_topic = req.body.topic || req.query.topic || process.env.MQTT_BASE_TOPIC;


  const mqtt = require('async-mqtt')  // require mqtt
  const mqtt_url = 'mqtt://'+process.env.MQTT_USER+':'+ process.env.MQTT_PASSWORD +'@127.0.0.1:'+process.env.MQTT_PORT;
  console.log( mqtt_url );
  const client = mqtt.connect('mqtt://127.0.0.1:'+(process.env.MQTT_PORT||'1883'), {
    clean: true,
    connectTimeout: 2800,
    clientId: 'DeerHttpApi',
    username: process.env.MQTT_USER||"",
    password: process.env.MQTT_PASSWORD||"",
  });
  try {
    await client.publish(msg_topic+'_'+msg_type,msg_content);    
  } catch (error) {
    console.log(error)  
  }finally {
    await client.end();
  }
  
   
  return res.status(200).json('done');
});

// Error handler
app.use(function (err, req, res, next) {
  console.error(err);
  res.status(500).send('Internal Serverless Error');
});

app.listen(80, () => {
  console.log(`Server start on http://localhost`);
});
