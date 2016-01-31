package area51.turboRocketWars.gui.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import area51.turboRocketWars.gui.MainMenuPanel;
import area51.turboRocketWars.gui.MainWindow;

public class MainWindowImpl extends JFrame implements MainWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<JPanel> panels = new ArrayList<JPanel>();
	JPanel masterPanel;
	public MainWindowImpl(){
		this.setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setVisible(true);
		setResizable(false);
	}
	public void addWindowPanel(JPanel... panel) {
		for(int i = 0; i < panel.length; i++){
			JPanel p = panel[i];
			int pX = getWidth() - getWidth()/(panel.length-i);
			int pY = 0;
			int pW = getWidth()/panel.length;
			int pH = getHeight();
			p.setBounds(pX, pY, pW, pH);
			p.setPreferredSize(new Dimension(pW, pH));
			
			getContentPane().add(p);
			panels.add(p);
			
		}
	}
	
	private JPanel menu;
	private MainMenuPanel mainMenu = new MainMenuPanelImpl();
	public void setMenu(){
		getContentPane().setBackground(Color.cyan);
		BufferedImage img = getScreenShot(getContentPane());
		clearWindow();
		
		mainMenu.setBounds(0, 0, getWidth(), getHeight());
		mainMenu.setBackground(img.getScaledInstance(getWidth(), getHeight(), 0));
		getContentPane().add(mainMenu.getPanel());
		
	}
	
	  public static BufferedImage getScreenShot(
			    Component component) {

			    BufferedImage image = new BufferedImage(
			      component.getWidth(),
			      component.getHeight(),
			      BufferedImage.TYPE_INT_RGB
			      );
			    // call the Component's paint method, using
			    // the Graphics object of the image.
			    component.paint( image.getGraphics() ); // alternately use .printAll(..)
			    return image;
			  }
	
	public void removeMenu(){
		if(menu != null){
			remove(menu);
			menu = null;
		}
	}
	
	private void resizePanels(){
		for(int i = 0; i < panels.size(); i++){
			int pX = getWidth() - getWidth()/(i+1);
			int pY = 0;
			int pW = getWidth()/panels.size();
			int pH = getHeight();
			panels.get(i).setBounds(pX, pY, pW, pH);
		}
	}

	public void clearWindow() {
//		for(JPanel p : panels) {
//			System.out.println(p.getX());
//			getContentPane().remove(p);
//			getContentPane().createImage(10, 10);
//			
//		}
		getContentPane().removeAll();
		getContentPane().revalidate();
		getContentPane().repaint();
		System.out.println("clearwindow");
	}
}
