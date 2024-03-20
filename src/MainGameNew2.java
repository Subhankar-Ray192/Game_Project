import java.util.*;

class GameAPI
{
	GameEnv env;
	
	Debug debug;
	
	Player human;
	Player agent;
	
	interface Player
	{	
		public int setMove(GameEnv env);
		
		public int getCurrToken();
	}
	
	class GameTree
	{
		int[] VALID_SCORE;
		
		GameEnv next_env;

		GameTree(GameEnv curr_env,int[] VALID_SCORE)
		{
			this.VALID_SCORE = VALID_SCORE;
			
			next_env = curr_env.boardCopy();
		}

		int[] minMax(int depth,int prev_token,int curr_token,boolean MaxPlayer)
		{	
			int best_move[] = new int[3];
			
			best_move[0] = next_env.boardScore(prev_token,VALID_SCORE);

			if(best_move[0] == VALID_SCORE[prev_token])
			{
				best_move[0] = best_move[0] + depth;
				return best_move;
			}

			if(best_move[0] == VALID_SCORE[curr_token])
			{
				best_move[0] = best_move[0] - depth;
				return best_move;
			}

			if(next_env.isGameEnd())
			{
				best_move[0] = best_move[0] - depth;
				return best_move;
			}

			if(MaxPlayer)
			{
				best_move[0] = Integer.MIN_VALUE;
						
				for(int row=0; row<next_env.SIZE; row++)
				{
				
					for(int col=0; col<next_env.SIZE; col++)
					{
						/*if(depth == 0)
						{
							System.out.println("Z");
						}*/
					
						if(next_env.isEmptyCell(row,col))
						{
							/*if(depth < 1)
							{
								System.out.println("Player-Maximum:"+MaxPlayer);
							}*/
						
							next_env.boardSetMove(row,col,curr_token);
						
							int curr_move[] = minMax(depth+1,curr_token,prev_token,!MaxPlayer);
						
							/*if(depth < 1)
							{
								System.out.println("Available_Moves:"+next_env.avail_cell_count);
							}*/
							
							/*if((best_move[0] < curr_move[0])&&(depth == 0))
							{
								
							}*/
							
							/*if(next_env.avail_cell_count == 4)
							{
								System.out.println("Maximizer:(Score,Best,Avail-Cell,Prev_Token,Curr_Token,Best_Row,Best_Col):"+MaxPlayer+","+curr_move[0]+","+best_move[0]+","+next_env.avail_cell_count+","+prev_token+","+curr_token+","+best_move[1]+","+best_move[2]+(best_move[0]>curr_move[0]));
								next_env.boardConfig(0);
								next_env.boardConfig(2);
								System.out.println();
							}*/

							if(best_move[0] < curr_move[0])
							{
								best_move[0] = curr_move[0];
								
								best_move[1] = row;
								best_move[2] = col;
							}

							next_env.boardClearMove(row,col);							

						}
					}
				}
			}
			else
			{
				best_move[0] = Integer.MAX_VALUE;
				
				for(int row=0; row<next_env.SIZE; row++)
				{
					/*if(depth == 0)
					{
						System.out.println("Y");
					}*/
				
					for(int col=0; col<next_env.SIZE; col++)
					{
						/*if(depth == 0)
						{
							System.out.println("Z");
						}*/
					
						if(next_env.isEmptyCell(row,col))
						{
							next_env.boardSetMove(row,col,curr_token);
						
							int curr_move[] = minMax(depth+1,curr_token,prev_token,!MaxPlayer);
						
							if(best_move[0] > curr_move[0])
							{
								best_move[0] = curr_move[0];	
							}

							next_env.boardClearMove(row,col);							

						}
					}
				}
			}
	return best_move;
		}
	}
	

	class GameEnv
	{
		final int[] VALID_SCORE = {0,10,-10};

		final int SIZE = 3;
		final int LIMIT = 9;
		
		int avail_cell_count;
		
		GameBoard board[][];
	
		class GameBoard
		{
			int s_cost;
			int d_cost;
		
			int curr_token;
			
			final int MIN_VAL = Integer.MIN_VALUE;
			final int[] AVAIL_S_COST = new int[]{2,7,6,9,5,1,4,3,8};
			
			GameBoard(int index)
			{	
				d_cost = 0;
				s_cost = AVAIL_S_COST[index];
				
				curr_token = 0;
			}
			
			int[] getCell()
			{
				int cell[] = new int[3];
				
				cell[0] = curr_token;
				cell[1] = s_cost;
				cell[2] = d_cost;
				
				return cell;
			}
		}
		
		GameEnv()
		{	
			avail_cell_count = LIMIT;
			
			board = new GameBoard[SIZE][SIZE];
			this.boardInit();
		}
		
		void boardInit()
		{
			for(int index=0; index<LIMIT; index++)
			{
				int row = index/SIZE;
				int col = index%SIZE;
				
				board[row][col] = new GameBoard(index);
			}
		}
		
		boolean isGameEnd()
		{
			if(avail_cell_count == 0)
			{
				return true;
			}
			return false;
		}
		
		boolean isEmptyCell(int row, int col)
		{
			if(board[row][col].getCell()[0] == 0)
			{
				return true;
			}
			return false;
		}
		
		void boardConfig(int pos)
		{	
			if(pos > 2)
			{
				return;
			}
			
			System.out.println("\nBoard Status:\n");
			for(int row=0; row<SIZE; row++)
			{
				for(int col=0; col<SIZE; col++)
				{
					System.out.print(board[row][col].getCell()[pos]+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
		
		GameEnv boardCopy()
		{
			GameEnv next_env = new GameEnv();
			
			next_env.avail_cell_count = this.avail_cell_count;
			
			for(int index=0; index<LIMIT; index++)
			{
				int row = index/SIZE;
				int col = index%SIZE;
 
				next_env.board[row][col].curr_token = this.board[row][col].curr_token;
			}
			return next_env;
		}
		
		void boardSetMove(int row, int col, int token)
		{
			this.avail_cell_count--;
			this.board[row][col].curr_token = token; 
		}

		void boardClearMove(int row, int col)
		{
			this.avail_cell_count++;
			this.board[row][col].curr_token = 0; 
		}
		
		int boardScore(int token, int[] VALID_SCORE)
		{
			UtilityFunction ufc = new UtilityFunction(token,board);
			
			if((ufc.isRow(SIZE))||(ufc.isCol(SIZE))||(ufc.isPrimaryDiag(SIZE))||(ufc.isSecondaryDiag(SIZE)))
			{
				return VALID_SCORE[token];
			}
			return VALID_SCORE[0];
		}

		class UtilityFunction
		{
			final int CONSTRAINT = 15;
			
			int token;
			
			GameBoard board[][];
			
			UtilityFunction(int token, GameBoard board[][])
			{
				this.token = token;
				
				this.board = board;
			}

			boolean isRow(int SIZE)
			{
				for(int row=0; row<SIZE; row++)
				{
					int sum = 0;
					for(int col=0; col<SIZE; col++)
					{
						if(board[row][col].getCell()[0] == token)
						{
							sum+=board[row][col].getCell()[1];
						}
					}

					if(sum == CONSTRAINT)
					{
						return true;
					}
				}
				return false;
			}

			boolean isCol(int SIZE)
			{
				for(int row=0; row<SIZE; row++)
				{
					int sum = 0;
					for(int col=0; col<SIZE; col++)
					{
						if(board[col][row].getCell()[0] == token)
						{
							sum+=board[col][row].getCell()[1];
						}
					}

					if(sum == CONSTRAINT)
					{
						return true;
					}
				}
				return false;
			}

			boolean isPrimaryDiag(int SIZE)
			{
				int sum = 0;
				for(int row=0; row<SIZE; row++)
				{	
					if(board[row][row].getCell()[0] == token)
					{
						sum+=board[row][row].getCell()[1];
					}
				}

				if(sum == CONSTRAINT)
				{
					return true;
				}
				return false;
			}

			boolean isSecondaryDiag(int SIZE)
			{
				int sum = 0;
				for(int row=SIZE-1,col=0; row>=0; row--,col++)
				{	
					if(board[col][row].getCell()[0] == token)
					{
						sum+=board[col][row].getCell()[1];
					}
				}

				if(sum == CONSTRAINT)
				{
					return true;
				}
				return false;
			}
		}
	}
	
	
	class Debug
	{
		int number_of_moves_played;
		
		Debug()
		{
			int number_of_moves_played = 0;	
		}
		
		void genNextBoardConfig(GameEnv env)
		{
			Scanner sc = new Scanner(System.in);
			
			System.out.print("\nEnter-number-of-moves:");
			number_of_moves_played = sc.nextInt();
			
			for(int i=0; i<number_of_moves_played; i++)
			{
				System.out.print("\nEnter-Row:");
				int row = sc.nextInt();
				
				System.out.print("\nEnter-Col:");
				int col = sc.nextInt();
				
				System.out.print("\nEnter-token:");
				int token = sc.nextInt();
				
				env.boardSetMove(row,col,token);
				
			}
			
			System.out.print("\nEnter-curr_token:");
			int curr_token = sc.nextInt();
			
			System.out.print("\nEnter-prev_token:");
			int prev_token = sc.nextInt();
			
			this.genNextMove(env,prev_token,curr_token);
		}
		
		void genNextMove(GameEnv env,int prev_token,int curr_token)
		{
			GameTree tree = new GameTree(env,env.VALID_SCORE);
			
			int best_moves[] = tree.minMax(0,prev_token,curr_token,true);
			System.out.println("Next-Best-Move:"+best_moves[1]+","+best_moves[2]);
			
			env.boardConfig(0);
			
			env.boardSetMove(best_moves[1],best_moves[2],curr_token);
			
			env.boardConfig(0);
		}
	}
	
	class Human implements Player
	{
		final int curr_token;
		private int error_move = 3;
		
		Human(int curr_token)
		{
			this.curr_token = curr_token;
		}
			
		public int setMove(GameEnv env)
		{
			Scanner sc = new Scanner(System.in);
			
			System.out.println("\nEnter Row:");
			int row = sc.nextInt();
			
			System.out.println("\nEnter Col:");
			int col = sc.nextInt();
			
			if(!env.isEmptyCell(row,col))
			{
				if(error_move > 0)
				{
					error_move--;
					
					return setMove(env);
				}
				else
				{
					env.boardSetMove(0,0,curr_token);
				}	
			}

			env.boardSetMove(row,col,curr_token);

			return env.boardScore(curr_token,env.VALID_SCORE);
		}
		
		public int getCurrToken()
		{
			return curr_token;
		}
	}
	
	class Agent implements Player
	{
		final int curr_token;
		final int prev_token;
		
		Agent(int curr_token,int prev_token)
		{
			this.curr_token = curr_token;
			this.prev_token = prev_token;
		}
		
		public int setMove(GameEnv env)
		{
			GameTree tree = new GameTree(env,env.VALID_SCORE);
			
			int best_moves[] = tree.minMax(0,prev_token,curr_token,true);
			System.out.println("Next-Best-Move:"+best_moves[1]+","+best_moves[2]);
			
			env.boardConfig(0);
			
			if(env.isEmptyCell(best_moves[1],best_moves[2]))
			{
				env.boardSetMove(best_moves[1],best_moves[2],curr_token);
			}
			
			env.boardConfig(0);
			
			return env.boardScore(curr_token,env.VALID_SCORE);
		}
		
		public int getCurrToken()
		{
			return curr_token;
		}
	}
	
	GameAPI()
	{
		env = new GameEnv();
		
		debug = new Debug();
		
		agent = new Agent(1,2);
		
		human = new Human(2);
	}
	
	void play()
	{
		int turn = 1;
		int score = 0;
		
		while(!env.isGameEnd())
		{
			if(turn%2 == 1)
			{
				score = agent.setMove(env);
				turn++;
			}
			else
			{
				score = human.setMove(env);
				turn++;
			}
			
			if(score == env.VALID_SCORE[agent.getCurrToken()])
			{
				System.out.println("AI WINS\n");
				break;
			}
			
			if(score == env.VALID_SCORE[human.getCurrToken()])
			{
				System.out.println("HUMAN WINS\n");
				break;
			}
		}
		
		if(score == env.VALID_SCORE[0])
		{
			System.out.println("GAME DRAWS\n");
		}
	}
}

class MainGameNew2
{
	public static void main(String args[])
	{
		GameAPI api = new GameAPI();
		
		api.env.boardConfig(0);
		
		//api.debug.genNextBoardConfig(api.env);
		
		api.play();
		
		api.env.boardConfig(0);
	}
}