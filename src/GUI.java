package src;

import javax.swing.*;
import java.awt.*;

public class GUI
{
	JFrame main_frame;
	JPanel main_panel;
	
	JPanel grid_panel;
	GameMatrix grid_button[][];
	
	JPanel text_box_panel;
	
	GameBox box;
	
	class GameBox
	{
		String result;
		String def_text = "Result will be shown here";
		
		JLabel res_label;
		
		GameBox()
		{
			this.result = "";
			
			this.res_label = new JLabel(def_text);
			res_label.setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
	
	class GameMatrix
	{	
		JButton btn;
		
		GameMatrix()
		{	
			this.btn = new JButton("");
		}
	}
	
	GUI()
	{
		this.main_frame = new JFrame("Tic Tac Toe");
		this.main_panel = new JPanel(new BorderLayout());
		this.grid_panel = new JPanel(new GridLayout(3,3));
		
		this.text_box_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		text_box_panel.setBackground(Color.WHITE);
		text_box_panel.setPreferredSize(new Dimension(500, 50));
		
		this.grid_button = new GameMatrix[3][3];
		this.box = new GameBox();
		
		this.grid_button_init();
		this.grid_panel_init();
		this.box_init();
		this.box_panel_init();
		this.main_panel_init();
		this.main_frame_init(500,500);
	}
	
	void box_init()
	{	
		text_box_panel.add(box.res_label);
	}
	
	void box_panel_init()
	{
		main_panel.add(text_box_panel, BorderLayout.SOUTH);
	}
	
	void grid_panel_init()
	{
		main_panel.add(grid_panel, BorderLayout.CENTER);
	}
	
	void main_panel_init()
	{	
		main_frame.add(main_panel);
	}
	
	void main_frame_init(int x, int y)
	{
		main_frame.setResizable(false);
		main_frame.setSize(x, y);
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void main_frame_activate()
	{
		main_frame.setVisible(true);
	}
	
	void grid_button_init()
	{
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				grid_button[i][j] = new GameMatrix();
				
				grid_panel.add(grid_button[i][j].btn);
			}
		}
	}
	
	public static void main(String[] args)
	{
		GUI ob = new GUI();
		ob.main_frame_activate();
	}
}