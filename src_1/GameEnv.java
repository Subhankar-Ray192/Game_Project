package api.env;

public class GameEnv
	{
		public final int[] VALID_SCORE = {0,10,-10};

		public final int SIZE = 3;
		public final int LIMIT = 9;
		
		private int avail_cell_count;
		
		GameBoard board[][];
	
		class GameBoard
		{
			private int s_cost;
			private int d_cost;
		
			private int curr_token;
			
			final int MIN_VAL = Integer.MIN_VALUE;
			final int[] AVAIL_S_COST = new int[]{2,7,6,9,5,1,4,3,8};
			
			GameBoard(int index)
			{	
				s_cost = AVAIL_S_COST[index];
				
				d_cost = 0;
				curr_token = 0;
			}
			
			public int[] getCell()
			{
				int cell[] = new int[3];
				
				cell[0] = curr_token;
				cell[1] = s_cost;
				cell[2] = d_cost;
				
				return cell;
			}
			
			public void setToken(int token)
			{
				curr_token = token;
			}
		}
		
		public GameEnv()
		{	
			avail_cell_count = LIMIT;
			
			board = new GameBoard[SIZE][SIZE];
			this.boardInit();
		}
		
		private void boardInit()
		{
			for(int index=0; index<LIMIT; index++)
			{
				int row = index/SIZE;
				int col = index%SIZE;
				
				board[row][col] = new GameBoard(index);
			}
		}
		
		public boolean isGameEnd()
		{
			if(avail_cell_count == 0)
			{
				return true;
			}
			return false;
		}
		
		public boolean isEmptyCell(int row, int col)
		{
			if(board[row][col].getCell()[0] == 0)
			{
				return true;
			}
			return false;
		}
		
		public void boardConfig(int pos)
		{	
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
		
		public GameEnv boardCopy()
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
		
		public void boardSetMove(int row, int col, int token)
		{
			this.avail_cell_count--;
			this.board[row][col].curr_token = token; 
		}

		public void boardClearMove(int row, int col)
		{
			this.avail_cell_count++;
			this.board[row][col].curr_token = 0; 
		}
		
		public int boardScore(int token, int[] VALID_SCORE)
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
			
			private int token;
			
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