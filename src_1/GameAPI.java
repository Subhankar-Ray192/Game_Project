package api;

import api.env.*;
import api.player.*;
import api.env.debug.*;

class GameAPI
{
	GameEnv env;
	
	Player human;
	Player agent;
	
	Debug debug;
	
	GameAPI()
	{
		env = new GameEnv();
		
		agent = new Agent(1,2);
		human = new Human(2);
		
		debug = new Debug();
	}
	
	void testAPI()
	{
		env.boardConfig(0);
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
	
	void actDebug()
	{
		debug.genNextBoardConfig(env);
	}
	
	public static void main(String args[])
	{
		GameAPI obj = new GameAPI();
		obj.testAPI();
		
		//obj.play();
		
		obj.actDebug();
	}
}