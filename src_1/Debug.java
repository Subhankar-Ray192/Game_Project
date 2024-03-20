package api.env.debug;

import api.env.*;
import api.env.tree.*;

import java.util.*;

public class Debug
	{
		int number_of_moves_played;
		
		public Debug()
		{
			int number_of_moves_played = 0;	
		}
		
		public void genNextBoardConfig(GameEnv env)
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