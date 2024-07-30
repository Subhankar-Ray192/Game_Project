<<<<<<< HEAD
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
=======
<<<<<<< HEAD
console.log("Hello World!")
=======
console.log("Hello World!");

const BASE_URL = 'https://lichess.org';
const BOT_TOKEN = 'lip_Ehh4RYcoZ3LLQNF9svTI'

function listenChallenge(){
	const res = await fetch(`${BASE_URL}/api/stream/event`, {
		headers: {
			Authorization: `Bearer ${BOT_TOKEN}`, 
		},
	});
	const reader = res.body.getReader();

	while(true) {
		const { value, done } = await reader.read();
		if(done) break;
		console.log('Received', value);
	}
>>>>>>> e4ed95f (Fixed typo & bug in the index.js code)
}

function init_bot(){
	listenChallenge();
}

<<<<<<< HEAD
init_bot()
=======
init_bot()
>>>>>>> 9be567c (Added GET request to lichess)
>>>>>>> e4ed95f (Fixed typo & bug in the index.js code)
