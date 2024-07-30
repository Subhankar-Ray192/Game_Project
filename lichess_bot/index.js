console.log("Hello World!");

const fetch = require('node-fetch');

require('dotenv').config();

const BASE_URL = 'https://lichess.org';
const {
	BOT_TOKEN,
} = process.env;

async function listenChallenge(){
	const res = await fetch(`${BASE_URL}/api/stream/event`, {
		headers: {
			Authorization: `Bearer ${BOT_TOKEN}`, 
		},
	});
	const reader = res.body.getReader();

	while(true) {
		const { value, done } = await reader.read();
		if(done) break;
		console.log('Received:', value);
	}
}

function init_bot(){
	listenChallenge();
}

init_bot()