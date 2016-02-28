package area51.turboRocketWars.gui.views;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import area51.turboRocketWars.Bodies.maps.Map;


public class MapSelectPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image background;

	private final static Color BUTTON_FONT_COLOR_NO_FOCUS = Color.red;
	private final static Color BUTTON_FONT_COLOR_FOCUS = Color.white;

	public final static String RETURN_ACTION = "Return";

	private static int BUTTON_MARGIN_LEFT = 0;
	private static int BUTTON_MARGIN_RIGHT = 0;
	private static int BUTTON_MARGIN_TOP = 30;
	private static int BUTTON_MARGIN_BOTTOM = 30;

	private static int BUTTON_FONT_SIZE = 20;
	private static int BUTTON_FONT_STYLE = Font.BOLD;
	private static String BUTTON_FONT_NAME = Font.SANS_SERIF;

	private JButton btnReturn;

	private JButton[] buttons;
	private JButton selectedButton;
	private int selectedIndex = 0;

	private ActionListener buttonListener;
	public MapSelectPanel(ActionListener buttonListener, Map[] maps){

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		btnReturn = new JButton("Return");

		this.buttonListener = buttonListener;

		buttons = new JButton[maps.length +1];
		if(maps != null){
			for(int i = 0; i < maps.length; i++){
				JButton b = new JButton(maps[i].getName());
				b.setActionCommand(maps[i].getName());
				setButtonStylesAndListener(b);
				buttons[i] = b;
			}
		}
		buttons[maps == null ? 0 : maps.length] = btnReturn;
		btnReturn.setActionCommand(RETURN_ACTION);
		selectedIndex = buttons.length-1;
		setButtonStylesAndListener(btnReturn);
		selectButton(btnReturn);
		
		setOpaque(false);
		setVisible(true);

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
