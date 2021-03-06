package area51.turboRocketWars.gui;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

public class MainWindow extends JLayeredPane implements ActionListener, Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean donePainting = false;
	private JFrame frame;
	private BufferedImage background;
	public MainWindow(){
		this.frame = new JFrame();
		this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//				this.frame.setBounds(0, 0, 800, 600);
		this.frame.setUndecorated(true);
		this.frame.setAlwaysOnTop(true);
		this.frame.setResizable(false);
		
//		setLayout(new GridBagLayout());
//		setLayout(new GridLayout(1, 2));
//		setLayout(new FlowLayout(FlowLayout.CENTER, 250, 0));
		
		this.frame.add(this);
		this.frame.setVisible(true);
		
		// disabling TAB and Shift+TAB. Otherwise focus can be lost to other components and keylistener will not work anymore
		KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, new HashSet<AWTKeyStroke>());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, new HashSet<AWTKeyStroke>());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, new HashSet<AWTKeyStroke>());
		KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, new HashSet<AWTKeyStroke>());
		
		EventQueue.invokeLater(this);
	}

	public void addLayer(JComponent layer, int x, int y, int width, int height) {

//		this.setPreferredSize(new Dimension(1920, 1080));
//		this.setMinimumSize(new Dimension(1920, 1080));
//		this.setSize(1920, 1080);
		
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.gridx = x;
//		gbc.gridy = y;
//		gbc.weightx = 0;
//		gbc.weighty = 0;
//		gbc.fill = GridBagConstraints.BOTH;
		
		layer.setBounds(x, y, width, height);
//		layer.setSize(width, height);
		
		System.out.println("x,y=" + x + "," + y + "\t" + "width,height=" + width + "," + height);
//		add(layer, gbc);
		add(layer);

	}

	@Override 
	public synchronized void addKeyListener(KeyListener l) {
		this.frame.addKeyListener(l);
	};
	
	@Override
	public synchronized KeyListener[] getKeyListeners() {
		return this.frame.getKeyListeners();
	}
	
	@Override
	public synchronized void removeKeyListener(KeyListener l) {
		this.frame.removeKeyListener(l);

	}

	public BufferedImage getScreenShot() {

		BufferedImage image = new BufferedImage(
				getWidth(),
				getHeight(),
				BufferedImage.TYPE_INT_RGB
				);
		// call the Component's paint method, using
		// the Graphics object of the image.
		frame.paint( image.getGraphics() ); 
		return image;
	}
	
	public void giveFocus(){
		this.frame.requestFocusInWindow();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (background != null) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage(background, 0, 0, this);
			g2d.dispose();
		}
	}

	public void setBackground(BufferedImage img){
		this.background = img;
		this.frame.setVisible(true);
		System.out.println("background set to: " + img.getHeight() + ";" + img.getWidth());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.repaint();
	}

	@Override
	public void run() {
		
		new Timer(20, this).start();
	}
}
