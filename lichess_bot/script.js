<<<<<<< HEAD
console.log("Hello World!")
=======
console.log("Hello World!");

const BASE_URL = 'https://lichess.org';
const BOT_TOKEN = 'lip_Ehh4RYcoZ3LLQNF9svTI'

function listenChallenge(){
	const res = await fetch(`${BASE_URL}/api/stream/event`, {
		header: {
			Authorization: `Bearer ${BOT_TOKEN}`, 
		},
	});
	const reader = res.body.getReader();

	while(true) {
		const { value, done } = await reader.read();
		if(done) break;
		console.log('Received', value);
	}
}

function init_bot(){
	listenChallenges();
}

init_bot()
>>>>>>> 9be567c (Added GET request to lichess)
