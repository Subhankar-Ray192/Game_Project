import java.util.*;

class Game
{
	GameEnv env;
	GameAgent agent;
	
	Human human;
	
	class StateSpace
	{
		int agent_win_counter;
		int agent_lose_counter;
		
		GameEnv next_env;
		
		StateSpace(GameEnv curr_env)
		{
			agent_win_counter = 0;
			agent_lose_counter = 0;
			
			next_env = curr_env.board_copy();
		}
		
		int gameScore(int depth,int token)
		{
			int best_score = 0;
			int score = next_env.board_score(token);
			
			if(score == 10)
			{
				agent_win_counter++;
				return score;
			}
			
			if(score == -10)
			{
				agent_lose_counter--;
				return score;
			}
			
			if(next_env.empty_slot<-1)
			{
				agent_win_counter++;
				return 0;
			}
			
			if((token-1) == 1)
			{
				best_score = 1000;
				
				for(int i=0; i<next_env.size; i++)
				{
					for(int j=0; j<next_env.size; j++)
					{
						if(next_env.isEmptySlot(i,j))
						{
							next_env.board_setMove(i,j,token);
							best_score = Math.min(best_score,gameScore(depth+1,(token+1)%2));
							next_env.board_clearMove(i,j);
						}
					}
				}
				return best_score;	
			}
			else
			{
				best_score = -1000;
				
				for(int i=0; i<next_env.size; i++)
				{
					for(int j=0; j<next_env.size; j++)
					{
						if(next_env.isEmptySlot(i,j))
						{
							next_env.board_setMove(i,j,token);
							best_score = Math.max(best_score,gameScore(depth+1,token+1));
							next_env.board_clearMove(i,j);
						}
					}
				}
				return best_score;
			}
		}
	}
	
	class GameEnv
	{
		int valid_scores[] = {10,-10};
		
		int size;
		int empty_slot;
		
		Tuple board[][];
		
		class Tuple
		{
			int static_cost;
			int dynamic_cost;
			
			int curr_token;
			
			int s_cost_list[] = {2,7,6,9,5,1,4,3,8};
			
			Tuple(int index)
			{
				this.static_cost = s_cost_list[index];
				
				this.curr_token = 0;
			}
		}
		
		GameEnv()
		{
			this.size = 3;
			this.empty_slot = 8;
			
			this.board = new Tuple[this.size][this.size];
			this.board_init();
		}
		
		void board_init()
		{
			for(int i=0; i<(this.size*this.size); i++)
			{
				int row = i/this.size;
				int col = i%this.size;
				this.board[row][col] = new Tuple(i);
			}
		}
	
		boolean isEmptySlot(int row, int col)
		{
			if(this.board[row][col].curr_token == 0)
			{
				return true;
			}
			return false;
		}
		
		GameEnv board_copy()
		{
			GameEnv next_env = new GameEnv();
			
			next_env.empty_slot = this.empty_slot;
			
			for(int i=0; i<(this.size*this.size); i++)
			{
				int row = i/this.size;
				int col = i%this.size;
				next_env.board[row][col].curr_token = this.board[row][col].curr_token;
			}
			return next_env;
		}
		
		void board_play_status()
		{
			System.out.println("\nCurrent-Status:\n");
			for(int i=0; i<this.size; i++)
			{
				for(int j=0; j<this.size; j++)
				{
					System.out.print(this.board[i][j].curr_token+" ");
				}
				System.out.println();
			}
			System.out.println();
		}

		void board_cost_status()
		{
			System.out.println("\nCurrent-Status:\n");
			for(int i=0; i<this.size; i++)
			{
				for(int j=0; j<this.size; j++)
				{
					System.out.print(this.board[i][j].dynamic_cost+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		void board_setMove(int row,int col, int token)
		{
			this.empty_slot = this.empty_slot - 1;
			this.board[row][col].curr_token = token; 
		}
		
		void board_clearMove(int row,int col)
		{
			this.empty_slot = this.empty_slot + 1;
			this.board[row][col].curr_token = 0; 
		}
		
		void board_setDynamicCost(int row,int col,int win_cost,int lose_cost)
		{
			int d_cost_value[] = {1,1,1,1,1,1,1,1,1};
				
			this.board[row][col].dynamic_cost = win_cost +lose_cost + d_cost_value[3*row+col];
		}
		
		int board_score(int token)
		{
			int p_diagonal = 0;
			int s_diagonal = 0;
			
			for(int i=0; i<this.size; i++)
			{
				int row_sum = 0;
				
				for(int j=0;j<this.size; j++)
				{
					if(this.board[i][j].curr_token == token)
					{
						row_sum+= this.board[i][j].static_cost;
					}
				}
				
				if(row_sum == 15)
				{
					return this.valid_scores[token-1];
				}
				
				int col_sum = 0;
				
				for(int j=0;j<this.size; j++)
				{
					if(this.board[i][j].curr_token == token)
					{
						col_sum+= this.board[i][j].static_cost;
					}
				}
				
				if(col_sum == 15)
				{
					return this.valid_scores[token-1];
				}
				
				if(this.board[i][i].curr_token == token)
				{
					p_diagonal+=this.board[i][i].static_cost;
				}
				
				if(this.board[i][this.size-i-1].curr_token == token)
				{
					s_diagonal+=this.board[i][this.size-i-1].static_cost;
				}
			}
			
			if(p_diagonal == 15)
				{
					return this.valid_scores[token-1];
				}
				
			if(s_diagonal == 15)
				{
					return this.valid_scores[token-1];
				}
				
			return 0;
		}
		
		int[] getMaxDynamicCost()
		{
			int best_move[] = {0,0};
			int max = -1000;
			
			for(int i=0; i<(this.size*this.size); i++)
			{
				int row = i/this.size;
				int col = i%this.size;
				
				if((isEmptySlot(row,col))&&(max < this.board[row][col].dynamic_cost))
				{
					max = this.board[row][col].dynamic_cost;
					best_move[0] = row;
					best_move[1] = col;
				}
			}
			return best_move;
		}
	}
	
	class GameAgent
	{
		int token;
		
		GameAgent()
		{
			token = 1;
		}
		
		void dynamicCost_init(GameEnv env)
		{
			for(int i=0; i<=env.empty_slot; i++)
			{
				StateSpace set = new StateSpace(env);
				
				int row = i/env.size;
				int col = i%env.size;
				
				if(env.isEmptySlot(row,col))
				{
					env.board_setMove(row,col,token);
				
					int score = set.gameScore(0,token);
					env.board_setDynamicCost(row,col,set.agent_win_counter,set.agent_lose_counter);
					
					env.board_clearMove(row,col);
				}
			}
		}
		
		void genMove(GameEnv env)
		{
			dynamicCost_init(env);
			
			int best_move[] =env.getMaxDynamicCost();
			env.board_setMove(best_move[0],best_move[1],token);
		}
	}
	
	class Human
	{
		int token;
		
		Scanner sc;

		Human()
		{
			token = 2;
			
			sc = new Scanner(System.in);
		}
		
		void genMove(GameEnv env)
		{
			System.out.print("\nEnter-Row:");
			int row = sc.nextInt();
			System.out.print("\nEnter-Col:");
			int col = sc.nextInt();
			
			if(env.isEmptySlot(row,col))
			{
				env.board_setMove(row,col,token);
			}
		}
	}
	
	Game()
	{
		env = new GameEnv();
		
		agent = new GameAgent();
		
		human = new Human();
	}
	
	void play_game()
	{
		int turn = 8;
		
		while(turn > -1)
		{
			if(turn%2 == 0)
			{
				System.out.print("\nAgent Move:\n");
				agent.genMove(env);
				env.board_play_status();
				env.board_cost_status();
				
				if(env.board_score(agent.token) == 10)
				{
					System.out.println("Agent Wins");
					break;
				}
			}
			else
			{
				System.out.print("\nHuman-Move:");
				human.genMove(env);
				env.board_play_status();
				env.board_cost_status();
				
				if(env.board_score(human.token) == -10)
				{
					System.out.println("Human Wins");
					break;
				}
			}
			turn--;
		}
		
		if(env.empty_slot < -1)
		{
			System.out.println("Game Draw");
		}
	}
}

class MainGameNew
{
	public static void main(String args[])
	{
		Game game = new Game();
		
		//game.env.board_play_status();
		//game.env.board_cost_status();
		
		game.play_game();
		
		//game.env.board_play_status();
		//game.env.board_cost_status();
	}
}