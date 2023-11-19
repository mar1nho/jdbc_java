import application.Menus;

import javax.swing.*;

public class Program implements Runnable {
	
	public static void main(String[] args) {
		Program program = new Program();
		program.run();
	}
	
	@Override
	public void run() {
		SwingUtilities.invokeLater(Menus::mainMenu);
	}
}