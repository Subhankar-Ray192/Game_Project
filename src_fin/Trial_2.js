class GameCell 
{
	constructor(index,token) 
	{
		this.s_cost = [2, 7, 6, 9, 5, 1, 4, 3, 8][index];
		this.d_cost = Number.MIN_SAFE_INTEGER;
	
		this.curr_token = token;
	}
}

class GameEnv
{
	constructor(size)
	{
		this.size = size;
		this.limit = size*size;
		this.availCell = size*size;
		
		this.board = [];
		this.initBoard();
	}
	
	initBoard()
	{
		for (let row = 0; row< this.size; row++) 
		{
			this.board[row] = [];
			for(let col = 0; col<this.size; col++)
			{
				let index = 3*row+col;
				
				const cell = new GameCell(index, 0);
				this.board[row][col] = cell;
			}
		}
	}
	
	boardConfig()
	{	
		console.log("\nBoard Status:\n");
		for(let row=0; row<this.size; row++)
		{
			let line = "";
			for(let col=0; col<this.size; col++)
			{
				console.log("("+row,col+"):",this.board[row][col].curr_token+",");
			}
			console.log("\n")
		}
	}
	
	isGameEnd() 
	{
		return this.availCell === 0;
	}
	
	isEmptyCell(row, col) 
	{
		return this.board[row][col].curr_token === 0;
	}
	
	boardCopy() 
	{
		let new_env = new GameEnv(this.size);
		
		new_env.availCell = this.availCell;
		
		for (let i = 0; i < this.size; i++) {
			for (let j = 0; j < this.size; j++) {
				new_env.board[i][j].curr_token = this.board[i][j].curr_token;
			}
		}
		return new_env;
	}
	
	boardSetMove(row, col, token) 
	{
		this.availCell--;
		this.board[row][col].curr_token = token;
	}
	
	boardClearMove(row, col) 
	{
		this.availCell++;
		this.board[row][col].curr_token = 0;
	}
	
	boardRefresh(cell,token_img)
	{
		for(let row=0; row<this.size; row++)
		{
			for(let col=0; col<this.size; col++)
			{
				let index = 3*row+col;
				
				cell[index].style.backgroundImage = `url(${token_img[0]})`;

			}
		}
	}
	
	boardSetImage(row,col,cell,token_img,token)
	{
		let index = row*3+col;
		
		cell[index].style.backgroundImage = `url(${token_img[token]})`;
	}
	
	boardAddClickEvent(move_history,cell,token_img,token)
	{	
		this.clickF = (event) => { 
			this.handleCellClick(event, cell, token_img, token); 
		};
		
		for(let row=0; row<this.size; row++)
		{
			for(let col=0; col<this.size; col++)
			{
				let index = 3*row + col;
				
				if(move_history[index])
				{
					continue;
				}
				
				console.log("11",this.clickF);
				
				cell[index].addEventListener('click', this.clickF);
			}
		}
	}
	
	handleCellClick(event,cell,token_img,token)
	{
		let t_cell = event.target;
		
		t_cell.style.backgroundImage = `url(${token_img[token]})`;
		
		document.dispatchEvent(new CustomEvent('cellClick', 
			{
        			detail: { index: Array.from(cell).indexOf(t_cell)}
			}));
	}
	
	boardClearClickEvent(move_history,cell,token_img)
	{
		for(let row=0; row<this.size; row++)
		{
			for(let col=0; col<this.size; col++)
			{
				let index = 3*row+col;
				
				if(move_history[index])
				{
					continue;
				}
				
				
				cell[index].removeEventListener('click',this.clickF);
				
				console.log("12",this.clickF);
			}
		}
	}
	
	boardScoreEval(token,valid_score)
	{
		let ufc = new Util(this.board,token);
			
		if((ufc.isRow(this.size))||(ufc.isCol(this.size))||(ufc.isPrimaryD(this.size))||(ufc.isSecondaryD(this.size)))
		{
			return valid_score[token];
		}
		return valid_score[0];
	}
}

class Util
{
	constructor(board,token)
	{
		this.constrain = 15;
		
		this.board = board;
		this.token = token;
	}
	
	isRow(size)
	{
		for(let row=0; row<size; row++)
				{
					let sum = 0;
					for(let col=0; col<size; col++)
					{
						if(this.board[row][col].curr_token == this.token)
						{
							sum+=this.board[row][col].s_cost;
						}
					}

					if(sum == this.constrain)
					{
						return true;
					}
				}
				return false;

	}
	
	isCol(size)
	{
		for(let row=0; row<size; row++)
				{
					let sum = 0;
					for(let col=0; col<size; col++)
					{
						if(this.board[col][row].curr_token == this.token)
						{
							sum+=this.board[col][row].s_cost;
						}
					}

					if(sum == this.constrain)
					{
						return true;
					}
				}
				return false;

	}

	isPrimaryD(size)
	{
		let sum = 0;
		for(let row=0; row<size; row++)
		{	
			if(this.board[row][row].curr_token == this.token)
			{
				sum+=this.board[row][row].s_cost;
			}
		}

		if(sum == this.constrain)
		{
			return true;
		}
		return false;
	}
	
	isSecondaryD(size)
	{
		let sum = 0;
		for(let row=0; row<size; row++)
		{	
			if(this.board[row][size-row-1].curr_token == this.token)
			{
				sum+=this.board[row][size-row-1].s_cost;
			}
		}

		if(sum == this.constrain)
		{
			return true;
		}
		return false;
	}
}

class GameTree
{
	constructor(env,avail_score)
	{
		this.new_env = env.boardCopy();
		
		this.avail_score = avail_score;
	}
	
	minMax(depth,prev_token,curr_token,maxPlayer)
	{
		let best_move = [];
			
		best_move[0] = this.new_env.boardScoreEval(prev_token,this.avail_score);
		
		if(best_move[0] == this.avail_score[prev_token])
		{
			best_move[0] = best_move[0] + depth;
			return best_move;
		}
		
		if(best_move[0] == this.avail_score[curr_token])
		{
			best_move[0] = best_move[0] - depth;
			return best_move;
		}
		
		if(this.new_env.isGameEnd())
		{
			best_move[0] = best_move[0] - depth;
			return best_move;
		}
		
		if(maxPlayer)
		{
			best_move[0] = Number.MIN_SAFE_INTEGER;
			
			for(let row=0; row<this.new_env.size; row++)
				{
					for(let col=0; col<this.new_env.size; col++)
					{
						if(this.new_env.isEmptyCell(row,col))
						{
							this.new_env.boardSetMove(row,col,curr_token);
						
							let curr_score = this.minMax(depth+1,curr_token,prev_token,!maxPlayer,this.best_move);
							
							if(best_move[0] < curr_score[0])
							{
								best_move[0] = curr_score[0];
								
								best_move[1] = row;
								best_move[2] = col;
							}

							this.new_env.boardClearMove(row,col);							

						}
					}
				}
		}
		else
		{
			best_move[0] = Number.MAX_SAFE_INTEGER;
			
			for(let row=0; row<this.new_env.size; row++)
				{
				
					for(let col=0; col<this.new_env.size; col++)
					{
					
						if(this.new_env.isEmptyCell(row,col))
						{
							this.new_env.boardSetMove(row,col,curr_token);
						
							let curr_score = this.minMax(depth+1,curr_token,prev_token,!maxPlayer,this.best_move);
						
							if(best_move[0] > curr_score[0])
							{
								best_move[0] = curr_score[0];	
							}

							this.new_env.boardClearMove(row,col);							

						}
					}
				}

		}
		
		return best_move;
		
	}
}


class Debug
{
	constructor(number_of_moves_played)
	{
		this.number_of_moves_played = number_of_moves_played;
	}

	genNextBoardConfig(env) 
	{
		let person = prompt("Please enter your name", "Harry Potter");
    		console.log(person);
		
		for (let i = 0; i < this.number_of_moves_played; i++) 
		{
			let row = prompt("Enter Row:");
			let col = prompt("Enter Col:");
			let token = prompt("Enter Token:");
        
     
			row = parseInt(row);
			col = parseInt(col);
			token = parseInt(token);

			env.boardSetMove(row, col, token);

		}

		let curr_token = prompt("Enter curr_token:");
		let prev_token = prompt("Enter prev_token:");

		curr_token = parseInt(curr_token);
		prev_token = parseInt(prev_token);
			
		this.genNextMove(env,prev_token,curr_token);
	}


	genNextMove(env,prev_token,curr_token)
	{
		let tree = new GameTree(env,[0,10,-10]);
			
		let best_move = tree.minMax(0,prev_token,curr_token,true);
		
		console.log("Next-Best-Move:"+best_move[1]+","+best_move[2]);
			
		env.boardConfig();
			
		env.boardSetMove(best_move[1],best_move[2],curr_token);
			
		env.boardConfig();
	}
}


class Player
{
	constructor(token)
	{
		this.token = token;
	}
}

class Human
{
	constructor(player)
	{
		this.player = player;
	}
	
	setData(row,col,env)
	{
		env.boardSetMove(row,col,player.token);
	}
}

class Agent
{
	constructor(player)
	{
		this.player = player;
	}
	
	getData(env,avail_score,human)
	{
		let tree = new GameTree(env,avail_score);
		
		let best_move = tree.minMax(0,human.player.token,this.player.token,true);
		
		return best_move;
	}
}



class GameCon
{
	constructor()
	{
		this.token_img = {
			0: 'blank_image.png',
			1: 'x_image.png',
			2: 'o_image.png'
		};

		this.cell = document.querySelectorAll('.cell');
		
		this.avail_score = [0,10,-10];
		
		this.env = new GameEnv(3);
		
		this.human = new Human(new Player(2));
		this.agent = new Agent(new Player(1));
		
	}
	
	startGame() {
		
		this.env.boardRefresh(this.cell, this.token_img);

		this.move_history = [0,0,0,0,0,0,0,0,0];
		this.turn_counter = 1;
		
		this.executeTurns();
	}
	
	
	async executeTurns() 
	{
    		while (this.turn_counter <= 9) 
		{
        		await this.agentTurn();
    		}
	}
	
	stopGame()
	{
		this.move_history = [0,0,0,0,0,0,0,0,0];
		
		this.env.boardClearClickEvent(this.move_history,this.cell,this.token_img);	
	}
	
	humanTurn()
	{		
		let move = [];
		let c_index;
					
		this.env.boardAddClickEvent(this.move_history, this.cell, this.token_img, this.human.player.token);
					
		document.addEventListener('cellClick', (event) => {

			c_index = event.detail.index;
									
			move[1] = Math.floor(c_index / this.env.size);
			move[2] = c_index % this.env.size; 
                    
			this.env.boardSetMove(move[1], move[2], this.human.player.token);
                    
			this.env.boardConfig();
			this.env.boardClearClickEvent(this.move_history, this.cell, this.token_img);

			this.move_history[c_index] = 1;
			
			console.log("Move History- Human",this.move_history);
		});
		
		this.turn_counter++;
	}
	
	async agentTurn()
	{		
		let move = [];
		move = this.agent.getData(this.env, this.avail_score, this.human);
			
		let index = 3 * move[1] + move[2];
					
		this.move_history[index] = 1; 

		this.env.boardSetImage(move[1], move[2], this.cell, this.token_img, this.agent.player.token);
		this.env.boardSetMove(move[1], move[2], this.agent.player.token);
					
		this.turn_counter++;

		console.log("Move History-AI:",this.move_history)
		
		if(this.turn_counter<9)
		{
			await this.humanTurn();
		}
	}
	
	
}


const game_con = new GameCon();

game_con.startGame();