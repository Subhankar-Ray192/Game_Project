package api.env.tree;

import api.env.*;

public class GameTree
	{
		public int[] VALID_SCORE;
		
		GameEnv next_env;

		public GameTree(GameEnv curr_env,int[] VALID_SCORE)
		{
			this.VALID_SCORE = VALID_SCORE;
			
			next_env = curr_env.boardCopy();
		}

		public int[] minMax(int depth,int prev_token,int curr_token,boolean MaxPlayer)
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