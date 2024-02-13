import java.util.*;

class GameEnv
{
	GameBoard board;
	
	int player_count;
	GameAgent player[];
	
	int win_counter[];
	
	class GameBoard
	{
		int priority_list[] = {2,1,2,1,3,1,2,1,2};
		
		String pieces[];
			
		int row;
		int col;
		
		Tuple obj[][];
		
		class Tuple
		{
			int weight;
			int value;
			int magic_list[] = {2,7,6,9,5,1,4,3,8};
			
			Tuple(int index)
			{
				this.weight = magic_list[index];

				this.value = 0;
			}
		}
		
		GameBoard()
		{	
			this.pieces = new String[3];
			this.pieces_init();
				
			this.row = 3;
			this.col = 3;
			
			this.obj = new Tuple[row][col];
			this.obj_init();
		}
		
		void pieces_init()
		{
			this.pieces[0] = "_";
			this.pieces[1] = "X";
			this.pieces[2] = "O";
		}
		
		void obj_init()
		{
			for(int i=0;i<row*col;i++)
			{
				obj[i/row][i%col] = new Tuple(i);
			}
		}
		
		void board_config()
		{
			System.out.println("\nGame-Status:\n");
			for(int i=0;i<row;i++)
			{
				for(int j=0; j<col; j++)
				{
					System.out.print(pieces[obj[i][j].value]+" ");
				}
				System.out.println();
			}
			System.out.println();
		}
	
		boolean board_constraints(int token)
		{
			int status = 0;
			
			for(int i=0;i<row;i++)
			{
				
				int col_sum = 0;
				
				for(int j=0;j<col;j++)
				{
					
					if(obj[i][j].value == token)
					{
						col_sum = col_sum + obj[i][j].weight;
					}
				}
				
				if(col_sum == 15)
				{
					status = 1;
					break;
				}

				int row_sum = 0;
				
				for(int j=0;j<col;j++)
				{
					if(obj[j][i].value == token)
					{
						row_sum = row_sum + obj[j][i].weight;
					}
				}
				
				if(row_sum == 15)
				{
					status = 1;
					break;
				}

			}
			
			int p_diag_sum = 0;
			
			for(int i=0;i<row;i++)
			{
				if(obj[i][i].value == token)
					{
						p_diag_sum = p_diag_sum + obj[i][i].weight;
					}
			}
			
			if(p_diag_sum == 15)
			{
				return true;
			}

			int rev_diag_sum = 0;
			
			for(int i=0;i<row;i++)
			{
				if(obj[row-1-i][row-i-1].value == token)
					{
						rev_diag_sum = rev_diag_sum + obj[row-1-i][row-1-i].weight;
					}
			}
			
			if(rev_diag_sum == 15)
			{
				return true;
			}
			
			if(status == 1)
			{
				return true;
			}
			return false;
		}
		
		boolean board_edge_constrains(int curr_row, int curr_col)
		{
			if((curr_row < 3)&&(curr_col < 3))
			{
				return true;
			}
			return false;
		}
		
	}
	
	class GameAgent
	{
		int token;
		
		
		
		GameAgent(int token)
		{
			this.token = token;
		}
		
		int rand_move(GameBoard board)
		{
			int best_row = (int)(Math.random()*10);
			int best_col = (int)(Math.random()*10);
			
			if(board.board_edge_constrains(best_row,best_col))
			{
				if(board.obj[best_row][best_col].value == 0)
				{
					board.obj[best_row][best_col].value = this.token;
					
					//System.out.println("Weight:"+board.obj[best_row][best_col].weight);
					System.out.println("R-Move:("+best_row+","+best_col+")");
				
					board.priority_list[(best_row*3+best_col)] = 0;
					
					return board.obj[best_row][best_col].weight;
				}			
			}
			return rand_move(board);
		}
		
		int smart_move(GameBoard board)
		{
			int best_val = 0;
			int index = 0;
			
			for(int i=0;i<(board.row*board.col);i++)
			{
				if(best_val < board.priority_list[i])
				{
					index = i;
					best_val = board.priority_list[i];
				}
				
			}
			
			System.out.println("S-Move:("+(index/3)+","+(index%3)+")");
			
			if(board.board_edge_constrains((index/3),(index%3)))
			{
				board.priority_list[index] = 0;
				board.obj[index/3][index%3].value = this.token;
			
				return board.obj[index/3][index%3].weight;
			}
			return smart_move(board);	
		}
	}
	
	GameEnv()
	{
		board = new GameBoard();
	
		player_count = 2;
		player = new GameAgent[player_count];
		this.player_init();
	
		win_counter = new int[player_count];
		this.win_counter_init();
	}
	
	void player_init()
	{
		this.player[0] = new GameAgent(1);
		this.player[1] = new GameAgent(2);
	}
	
	void win_counter_init()
	{
		this.win_counter[0] = 0;
		this.win_counter[1] = 0;
	}
	
	void play_game()
	{
		int turn = 0;
		
		while(turn < 9)
		{
			if(turn%player_count == 0)
			{
				win_counter[turn%player_count] += player[turn%player_count].rand_move(board);
			}
			else
			{
				win_counter[turn%player_count] += player[turn%player_count].smart_move(board);
			}
			
			System.out.println(board.board_constraints(player[turn%player_count].token));
			board.board_config();
			
			if(board.board_constraints(player[turn%player_count].token))
			{
				System.out.println("Game Over");
				break;
			}
			
			//System.out.println("\nwin_counter("+win_counter[0]+","+win_counter[1]+")");
			
			turn++;
		}
		
	}
	
}

class TicTacToeGame
{
	public static void main(String args[])
	{
		GameEnv env = new GameEnv();
	
		env.board.board_config();	
		env.play_game();
		env.board.board_config();
	}
}