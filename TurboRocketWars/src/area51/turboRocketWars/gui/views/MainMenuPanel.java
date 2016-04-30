package area51.turboRocketWars.gui.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		
	private static Color BUTTON_FONT_COLOR_NO_FOCUS = Color.red;
	private static Color BUTTON_FONT_COLOR_FOCUS = Color.white;
	
	public static final String START_GAME_ACTION = "Start Game";
	public static final String MAP_SELECT_ACTION = "Select Map";
	public static final String SETTINGS_ACTION = "Settings";
	public static final String CANCEL_ACTION = "Cancel";
	public static final String EXIT_ACTION = "Exit";
	
	private static int BUTTON_FONT_SIZE = 50;
	private static int BUTTON_FONT_STYLE = Font.BOLD;
	private static String BUTTON_FONT_NAME = Font.SANS_SERIF;
	
	private JButton btnStartGame;
	private JButton btnMapSelect;
	private JButton btnSettings;
	private JButton btnCancel;
	private JButton btnExit;
	
	private JButton[] buttons;
	private JButton selectedButton;
	private int selectedIndex = 0;
	
	private ActionListener buttonListener;
	public MainMenuPanel(ActionListener buttonListener){
		
		setLayout(new GridLayout(6, 0));
		
		JLabel lblTitle = new JLabel(TITLE);
		btnStartGame = new JButton("Start Game");
		btnMapSelect = new JButton("Select Map");
		btnSettings = new JButton("Settings");
		btnCancel = new JButton("Cancel");
		btnExit = new JButton("Exit");
		
		this.buttonListener = buttonListener;
		
		lblTitle.setFont(new Font(LABEL_FONT_NAME, LABEL_FONT_STYLE, LABEL_FONT_SIZE));
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		
		btnStartGame.setActionCommand(START_GAME_ACTION);
		btnMapSelect.setActionCommand(MAP_SELECT_ACTION);
		btnSettings.setActionCommand(SETTINGS_ACTION);
		btnCancel.setActionCommand(CANCEL_ACTION);
		btnExit.setActionCommand(EXIT_ACTION);
		
		add(lblTitle);
		setButtonStylesAndListener(btnStartGame);
		setButtonStylesAndListener(btnMapSelect);
		setButtonStylesAndListener(btnSettings);
		setButtonStylesAndListener(btnCancel);
		setButtonStylesAndListener(btnExit);

		setOpaque(false);
		setVisible(true);
		
		selectButton(btnStartGame);
		buttons = new JButton[]{btnStartGame, btnMapSelect, btnSettings, btnCancel, btnExit};
		
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
		c.setContentAreaFilled(false);
		c.setFocusPainted(false);
		c.setBorderPainted(false);
		c.setFont(new Font(BUTTON_FONT_NAME, BUTTON_FONT_STYLE, BUTTON_FONT_SIZE));
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
