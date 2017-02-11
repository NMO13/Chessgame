package gui;

public class HumanPlayerGUIAppRun extends Thread {
	
	HumanPlayerGUIAppWindow window = null;
	public HumanPlayerGUIAppRun(HumanPlayerGUIAppWindow window) {
		this.window = window;
	}
	
	public void run() {
		window.setBounds(10, 10, 420, 180);
		window.setVisible(true);
	}
}
