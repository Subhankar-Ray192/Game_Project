console.log("Hello World!");

const https = require('node:https');
require('dotenv').config();

const {
	BOT_TOKEN,
} = process.env;

async function listenChallenge(){
	
	const https = require('node:https');

	const options = {
  		hostname: 'lichess.org',
  		port: 443,
  		path: '/api/stream/event',
  		method: 'GET',
		headers: {
			Authorization: `Bearer ${BOT_TOKEN}`,
		},
	};

	const req = https.request(options, (res) => {
  	console.log('statusCode:', res.statusCode);
  	console.log('headers:', res.headers);

  	res.on('data', (data) => {
    		console.log(data);
  		});
	});

	req.on('error', (e) => {
  		console.error(e);
	});
	req.end()
}

function init_bot(){
	listenChallenge();
}

init_bot()