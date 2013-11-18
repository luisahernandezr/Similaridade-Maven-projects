package view;

import javax.swing.JFrame;

import controller.PrincipalWindow;

@SuppressWarnings("serial")
public class MainClass extends JFrame 
{

	/**
	 * Main that controls all the basic interface items
	 * @param arg
	 */
	public static void main(String arg[]) 
	{
		PrincipalWindow p=new PrincipalWindow();
		
		p.setBounds(0, 0, 1000, 700);
		p.setVisible(true);
		p.setTitle("SOFTWARE PLUGINS SIMILARITY");
		p.setLocationRelativeTo(null);
		p.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}