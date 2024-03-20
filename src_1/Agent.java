package api.player;

import api.env.*;
import api.env.tree.*;

public class Agent implements Player
	{
		final int curr_token;
		final int prev_token;
		
		public Agent(int curr_token,int prev_token)
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