package api.player;

import api.env.*;

public interface Player
	{	
		public int setMove(GameEnv env);
		
		public int getCurrToken();
	}