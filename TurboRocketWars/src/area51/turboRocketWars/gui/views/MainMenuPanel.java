package area51.turboRocketWars.gui.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class MainMenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image background;
	
	private static String TITLE = "TurboRocket";
	
	private static int LABEL_FONT_SIZE = 50;
	private static int LABEL_FONT_STYLE = Font.BOLD+Font.ITALIC;
	private static String LABEL_FONT_NAME = Font.SERIF;
	
	private static int LABEL_MARGIN_LEFT = 0;
	private static int LABEL_MARGIN_RIGHT = 0;
	private static int LABEL_MARGIN_TOP = 50;
	private static int LABEL_MARGIN_BOTTOM = 90;
		
	private static Color BUTTON_FONT_COLOR_NO_FOCUS = Color.red;
	private static Color BUTTON_FONT_COLOR_FOCUS = Color.white;
	
	public static final String START_GAME_ACTION = "Start Game";
	public static final String MAP_SELECT_ACTION = "Select Map";
	public static final String CANCEL_ACTION = "Cancel";
	public static final String EXIT_ACTION = "Exit";
	
	private static int BUTTON_MARGIN_LEFT = 0;
	private static int BUTTON_MARGIN_RIGHT = 0;
	private static int BUTTON_MARGIN_TOP = 30;
	private static int BUTTON_MARGIN_BOTTOM = 30;
	
	private static int BUTTON_FONT_SIZE = 50;
	private static int BUTTON_FONT_STYLE = Font.BOLD;
	private static String BUTTON_FONT_NAME = Font.SANS_SERIF;
	
	private JButton btnStartGame;
	private JButton btnMapSelect;
	private JButton btnCancel;
	private JButton btnExit;
	
	private JButton[] buttons;
	private JButton selectedButton;
	private int selectedIndex = 0;
	
	private ActionListener buttonListener;
	public MainMenuPanel(ActionListener buttonListener){
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel lblTitle = new JLabel(TITLE);
		btnStartGame = new JButton("Start Game");
		btnMapSelect = new JButton("Select Map");
		btnCancel = new JButton("Cancel");
		btnExit = new JButton("Exit");
		
		this.buttonListener = buttonListener;
		
		lblTitle.setFont(new Font(LABEL_FONT_NAME, LABEL_FONT_STYLE, LABEL_FONT_SIZE));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		Border lblPadding = new EmptyBorder(LABEL_MARGIN_TOP, LABEL_MARGIN_LEFT, LABEL_MARGIN_BOTTOM, LABEL_MARGIN_RIGHT);
		lblTitle.setBorder(lblPadding);
		
		btnStartGame.setActionCommand(START_GAME_ACTION);
		btnMapSelect.setActionCommand(MAP_SELECT_ACTION);
		btnCancel.setActionCommand(CANCEL_ACTION);
		btnExit.setActionCommand(EXIT_ACTION);
		
		add(lblTitle);
		setButtonStylesAndListener(btnStartGame);
		setButtonStylesAndListener(btnMapSelect);
		setButtonStylesAndListener(btnCancel);
		setButtonStylesAndListener(btnExit);
		
		
//		add(btnStartGame);
//		add(btnMapSelect);
//		add(btnCancel);
//		add(btnExit);

		setOpaque(false);
		setVisible(true);
		
		selectButton(btnStartGame);
		buttons = new JButton[]{btnStartGame, btnMapSelect, btnCancel, btnExit};
		
	}
	
	private void selectButton(JButton button){
		if(selectedButton != null) selectedButton.setForeground(BUTTON_FONT_COLOR_NO_FOCUS);
		selectedButton = button;
		selectedButton.setForeground(BUTTON_FONT_COLOR_FOCUS);	
	}
	
	public JButton getSelectedButton(){
		return this.selectedButton;
	}
	
	public void focusNextButton(){
		selectButton(buttons[(selectedIndex=(selectedIndex+1)%buttons.length)]);
	}
	public void focusPrevButton(){
		selectButton(buttons[(selectedIndex=((selectedIndex == 0 ? buttons.length : selectedIndex) -1)%buttons.length)]);
	}
	
	private void setButtonStylesAndListener(JButton c){
		c.setForeground(BUTTON_FONT_COLOR_NO_FOCUS);
		c.setAlignmentX(CENTER_ALIGNMENT);
		c.setContentAreaFilled(false);
		c.setEnabled(false);
		c.setBorderPainted(false);
		c.setFont(new Font(BUTTON_FONT_NAME, BUTTON_FONT_STYLE, BUTTON_FONT_SIZE));
		c.setMargin(new Insets(BUTTON_MARGIN_TOP, BUTTON_MARGIN_LEFT, BUTTON_MARGIN_BOTTOM, BUTTON_MARGIN_RIGHT));
		c.addActionListener(buttonListener);
		add(c);
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, null);
	};

	public void setBackground(Image img) {
		this.background = img;
	}
	
	public JPanel getPanel(){
		return this;
	}
}
