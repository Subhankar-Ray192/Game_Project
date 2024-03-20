import java.util.*;

class Game
{
	GameEnv env;
	GameAgent agent;
	
	Human human;
	
	Debugger debugger;
	
	class StateSpace
	{
		GameEnv next_env;
		
		int chk_flag;
		
		StateSpace(GameEnv curr_env)
		{
			next_env = curr_env.board_copy();
			
			chk_flag = 0;
		}
		
		int[] gameScore(int depth,int prev_token,int curr_token)
		{
			int best_moves[] = new int[3];
			
			best_moves[0] = next_env.board_score(prev_token);
			
			if(best_moves[0] == 10)
			{
				best_moves[0] = best_moves[0] - depth; 
				return best_moves;
			}
			
			if(best_moves[0] == -10)
			{
				best_moves[0] = best_moves[0] + depth;
				return best_moves;
			}
			
			if(next_env.empty_slot == -1)
			{
				best_moves[0] = -depth;
				return best_moves;
			}
			
			if(curr_token == 2)
			{
				best_moves[0] = Integer.MAX_VALUE;
				best_moves[1] = 0;
				best_moves[2] = 0;
				
				for(int i=0; i<next_env.size; i++)
				{
					for(int j=0; j<next_env.size; j++)
					{
						if(next_env.isEmptySlot(i,j))
						{
							next_env.board_setMove(i,j,curr_token);
							int[] curr_moves = gameScore(depth+1,curr_token,prev_token);
							
							int tmp = best_moves[0];
							
							if(best_moves[0] > curr_moves[0])
							{
								best_moves[1] = i;
								best_moves[2] = j;
								best_moves[0] = curr_moves[0];
							}
							
							if(next_env.empty_slot == 3)
							{
								System.out.println("Maximizer:(Score,Best,Avail-Cell,Prev_Token,Curr_Token,Best_Row,Best_Col):"+curr_moves[0]+","+tmp+","+next_env.empty_slot+","+prev_token+","+curr_token+","+best_moves[1]+","+best_moves[2]+(tmp>curr_moves[0]));
								next_env.board_play_status();
								System.out.println();
								chk_flag++;
							}

							next_env.board_clearMove(i,j);
						}
					}
				}
				return best_moves;	
			}
			else
			{
				best_moves[0] = Integer.MIN_VALUE;
				best_moves[1] = 0;
				best_moves[2] = 0;
				
				int best_row = 0;
				int best_col = 0;
				
				for(int i=0; i<next_env.size; i++)
				{
					for(int j=0; j<next_env.size; j++)
					{	
						if(next_env.isEmptySlot(i,j))
						{
							
							next_env.board_setMove(i,j,curr_token);

							int[] curr_moves = gameScore(depth+1,curr_token,prev_token);
							
							int tmp = best_moves[0];
							
							if(best_moves[0] < curr_moves[0])
							{
								best_moves[1] = i;
								best_moves[2] = j;
								
								best_row = i;
								best_col = j;
								
								best_moves[0] = curr_moves[0];
							}
							
							if(next_env.empty_slot == 3)
							{
								System.out.println("Maximizer:(Score,Best,Avail-Cell,Prev_Token,Curr_Token,Best_Row,Best_Col):"+curr_moves[0]+","+tmp+","+next_env.empty_slot+","+prev_token+","+curr_token+","+best_moves[1]+","+best_moves[2]+(tmp<curr_moves[0]));
								System.out.println(i+","+j+","+best_row+","+best_col);
								next_env.board_play_status();
								System.out.println();
								chk_flag++;
							}
							
							next_env.board_clearMove(i,j);
						}
					}
				}
				
				return best_moves;
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
			int d_cost_list[] = {Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,3,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE,Integer.MIN_VALUE};
			
			Tuple(int index)
			{
				this.static_cost = s_cost_list[index];
				this.dynamic_cost = d_cost_list[index];
				
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
		
		void board_setDynamicCost(int row,int col,int cost)
		{
				
			this.board[row][col].dynamic_cost = cost;
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
					if(this.board[j][i].curr_token == token)
					{
						col_sum+= this.board[j][i].static_cost;
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
		
		void dynamicCost_init(GameEnv env, Human human)
		{
			StateSpace set = new StateSpace(env);
				
			int[] best_moves = set.gameScore(0,human.token,this.token);
			env.board_setDynamicCost(best_moves[1],best_moves[2],best_moves[0]);
			
		}
		
		void genMove(GameEnv env)
		{
			
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
	
	class Debugger
	{
		int number_of_moves;

		Scanner sc;
		 		
		Debugger()
		{
			number_of_moves = 0;
			
			sc = new Scanner(System.in);
		}
		
		void debug_moves(GameEnv env)
		{
			System.out.print("\nEnter-number-of-moves:");
			number_of_moves = sc.nextInt();
			
			for(int i=0; i<number_of_moves; i++)
			{
				System.out.print("\nEnter-Row:");
				int row = sc.nextInt();
				
				System.out.print("\nEnter-Col:");
				int col = sc.nextInt();
				
				System.out.print("\nEnter-token:");
				int token = sc.nextInt();
				
				env.board_setMove(row,col,token);
				
			}
			
			StateSpace set = new StateSpace(env);
			
			System.out.print("\nEnter-curr_token:");
			int curr_token = sc.nextInt();
			
			System.out.print("\nEnter-prev_token:");
			int prev_token = sc.nextInt();
			
			int best_moves[] = set.gameScore(0,prev_token,curr_token);
			System.out.println("Best-Moves:"+best_moves[1]+","+best_moves[2]);
			
			env.board_setDynamicCost(best_moves[1],best_moves[2],best_moves[0]);
			
			env.board_play_status();
			env.board_cost_status();
			
			env.board_setMove(best_moves[1],best_moves[2],curr_token);
			
			env.board_play_status();
			env.board_cost_status();
		}
	}
	
	Game()
	{
		env = new GameEnv();
		
		agent = new GameAgent();
		
		human = new Human();
		
		debugger = new Debugger();
	}
	
	void play_game()
	{
		int turn = 8;
		
		while(turn >= 0)
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

				agent.dynamicCost_init(env,human);
			}

			turn--;
		}
		
		if(env.empty_slot < -1)
		{
			System.out.println("Game Draw");
		}
	}
}

class MainGameNew1
{
	public static void main(String args[])
	{
		Game game = new Game();
		
		//game.env.board_play_status();
		//game.env.board_cost_status();
		
		game.play_game();
		
		//game.env.board_play_status();
		//game.env.board_cost_status();
		
		//game.debugger.debug_moves(game.env);
	}
}