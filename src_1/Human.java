package api.player;

import api.env.*;
import java.util.*;

public class Human implements Player
	{
		final int curr_token;
		private int error_move = 3;
		
		public Human(int curr_token)
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