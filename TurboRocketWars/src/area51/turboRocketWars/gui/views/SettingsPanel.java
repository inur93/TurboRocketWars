package area51.turboRocketWars.gui.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import area51.turboRocketWars.exceptions.SettingTypeException;
import area51.turboRocketWars.settings.Setting;

public class SettingsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Font TITLE_FONT = new Font(Font.SERIF, Font.BOLD+Font.ITALIC, 50);
	private static Border TITLE_MARGIN = new EmptyBorder(50, 0, 90, 0);
	
	public static final String RETURN_ACTION = "Return";
	
	private static final Font ENTRY_NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	private static final Color ENTRY_NAME_COLOR_NO_FOCUS = Color.red;
	private static final Color ENTRY_NAME_COLOR_FOCUS = Color.white;
	
	private static final Font ENTRY_VALUE_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
	private static final Color ENTRY_VALUE_COLOR = Color.red;
	private static final Dimension ENTRY_VALUE_DIMENSION = new Dimension(100, 20);
	
	private ArrayList<JButton> buttons = new ArrayList<JButton>();
	private HashMap<JButton, JTextField> settingValues = new HashMap<JButton, JTextField>();
	
	private JButton selectedButton;
	private int selectedIndex = 0;
	
	private ActionListener buttonListener;
	private KeyListener keyListener;
	
	
	// title constraints
	private static final int lblGridx = 0;
	private static final int lblGridy = 0;
	private static final int lblGridWidth = 10;
	private static final double lblWeightx = 0;
	private static final double lblWeighty = 0;
	private static final int lblFill = GridBagConstraints.NONE;
	private static final int lblAnchor = GridBagConstraints.CENTER;

	// setting name constraints
	private static final int settingNameGridx = 1;
	private static final int settingNameGridWidth = 3;
	private static final double settingNameWeightx = 0.5;
	private static final double settingNameWeighty = 1.0;
	private static int settingNameFill = GridBagConstraints.NONE;
	private static int settingNameAnchor = GridBagConstraints.LINE_END;
	private static final Insets settingNameInsets = new Insets(0, 0, 0, 10);

	// value constraints
	private static final int settingValueGridx = 5;
	private static final int settingValueGridWidth = 3;
	private static final double settingValueWeightx = 0.5;
	private static final double settingValueWeighty = 1.0;
	private static final int settingValueFill = GridBagConstraints.NONE;
	private static final int settingValueAnchor = GridBagConstraints.LINE_START;
	private static final Insets settingValueInsets = new Insets(0, 10, 0, 0);
	
	/**
	 * Used for the settings overview, redirecting to other settings. options are shown in one column, centered.
	 * Only names shown, values are hidden
	 * @param title name shown at the top of window
	 * @param options this will be used for button's text. Name and value equivalent to {@link Setting} is the same
	 * @param buttonListener is added on buttons, to catch actionCommand on click or button.doClick()
	 * @param extraActions for extra buttons with these actionCommands. Return button is added by default
	 */
	public SettingsPanel(String title, List<String> options, ActionListener buttonListener, String... extraActions){
		GridBagConstraints c = new GridBagConstraints();
		preInitialize(title, buttonListener, keyListener, c);
		
		settingNameFill = GridBagConstraints.HORIZONTAL;
		settingNameAnchor = GridBagConstraints.CENTER;
		
		int level = 1;
		for(String s : options){
			JButton button = setSettingNameStyle(new JButton(s));
			button.setActionCommand(button.getText());
				
			setSettingNameConstraints(c, level);
			add(button, c);
			
			level++;
			buttons.add(button);
			settingValues.put(button, new JTextField(button.getText()));
		}
		
		postInitialize(level, extraActions, c);
	}
	
	/**
	 * Showing {@link Setting} names on left side, values on right. 
	 * @param title name shown at the top of window
	 * @param settings the list shown name on left side and values on right
	 * @param buttonListener added to buttons. used to catch actionCommand on click or button.doClick()
	 * @param keyListener added to textFields to still be able to get keyEvents when textField has focus
	 */
	public SettingsPanel(String title, Iterator<Entry<String, Setting>> settings, ActionListener buttonListener, KeyListener keyListener, String... extraActions){		
		
		GridBagConstraints c = new GridBagConstraints();
		preInitialize(title, buttonListener, keyListener, c);
		
		settingNameFill = GridBagConstraints.NONE;
		settingNameAnchor = GridBagConstraints.LINE_END;
		
		int level = 1;
		while(settings.hasNext()){
			Entry<String, Setting> e = settings.next();
			Setting entry = e.getValue();
			JButton button = setSettingNameStyle(new JButton(entry.getName()));
			button.setActionCommand(button.getText());
			
			JTextField value = null;
			try {
				value = setSettingValueStyle(new JTextField(String.valueOf(entry.getValue(String.class))));
			} catch (SettingTypeException e1) {
				e1.printStackTrace();
			}
			setSettingNameConstraints(c, level);
			add(button, c);
			setSettingValueConstraints(c, level);
			add(value, c);
			level++;
			buttons.add(button);
			settingValues.put(button, value);
		}
				
		postInitialize(level, extraActions, c);
	}
	
	private void preInitialize(String title, ActionListener buttonListener, KeyListener keyListener, GridBagConstraints c){
		this.buttonListener = buttonListener;
		this.keyListener = keyListener;
		
		setLayout(new GridBagLayout());
		
		JLabel lblTitle = new JLabel(title);
	
		setLayout(new GridBagLayout());	
		setOpaque(false);
		// adding title label
		lblTitle.setFont(TITLE_FONT);
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		Border lblPadding = TITLE_MARGIN;
		lblTitle.setBorder(lblPadding);

		setTitleConstraints(c);
		add(lblTitle, c);
	}
	
	private void postInitialize(int level, String[] extraActions, GridBagConstraints c){
		
		if(extraActions != null)
		for(String s : extraActions){
			setSettingNameConstraints(c, level++);
			JButton btn = setSettingNameStyle(new JButton(s));
			btn.setActionCommand(s);
			add(btn, c);
			buttons.add(btn);
		}
		
		setSettingNameConstraints(c, level++);
		JButton btnReturn = setSettingNameStyle(new JButton(RETURN_ACTION));
		btnReturn.setActionCommand(RETURN_ACTION);
		add(btnReturn, c);
		buttons.add(btnReturn);
		setOpaque(false);
		setVisible(true);
		
		selectButton(btnReturn);
		selectedIndex = buttons.indexOf(btnReturn);	
	}
	
	private void setTitleConstraints(GridBagConstraints c){
		c.gridx = lblGridx;
		c.gridy = lblGridy;
		c.gridwidth = lblGridWidth;
		c.weighty = lblWeighty;
		c.weightx = lblWeightx;
		c.fill = lblFill;
		c.anchor = lblAnchor;
	}

	private void setSettingNameConstraints(GridBagConstraints c, int level){
		c.gridx = settingNameGridx;
		c.gridy = level;
		c.gridwidth = settingNameGridWidth;
		c.weightx = settingNameWeightx;
		c.weighty = settingNameWeighty;
		c.anchor = settingNameAnchor;
		c.fill = settingNameFill;
		c.insets = settingNameInsets;
	}
	
	private void setSettingValueConstraints(GridBagConstraints c, int level){
		c.gridx = settingValueGridx;
		c.gridy = level;
		c.gridwidth = settingValueGridWidth;
		c.weightx = settingValueWeightx;
		c.weighty = settingValueWeighty;
		c.anchor = settingValueAnchor;
		c.fill = settingValueFill;
		c.insets = settingValueInsets;
	}
	
	private JButton setSettingNameStyle(JButton button){
		button.setOpaque(false);
		button.setFont(ENTRY_NAME_FONT);
		button.setForeground(ENTRY_NAME_COLOR_NO_FOCUS);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.addActionListener(buttonListener);
		button.setContentAreaFilled(false);
		return button;
	}
	
	private JTextField setSettingValueStyle(JTextField field){
		field.setOpaque(false);
		field.setFont(ENTRY_VALUE_FONT);
		field.setForeground(ENTRY_VALUE_COLOR);
		field.setBorder(BorderFactory.createEmptyBorder());
		field.addKeyListener(keyListener);
		field.setPreferredSize(ENTRY_VALUE_DIMENSION);
		return field;
	}
	
	private void selectButton(JButton button){
		if(selectedButton != null) selectedButton.setForeground(ENTRY_NAME_COLOR_NO_FOCUS);
		selectedButton = button;
		selectedButton.setForeground(ENTRY_NAME_COLOR_FOCUS);
		JTextField field = settingValues.get(selectedButton);
		if(field != null)field.requestFocusInWindow();
	}
	
	public JButton getSelectedButton(){
		return this.selectedButton;
	}
	
	public String getSelectedValue(){
		JTextField field = this.settingValues.get(selectedButton);
		if(field != null) return field.getText();
		return null;
	}
	
	public void focusNextButton(){
		selectButton(buttons.get((selectedIndex=(selectedIndex+1)%buttons.size())));
	}
	public void focusPrevButton(){
		selectButton(buttons.get((selectedIndex=((selectedIndex == 0 ? buttons.size() : selectedIndex) -1)%buttons.size())));
	}
	
	public JPanel getPanel(){
		return this;
	}
}

