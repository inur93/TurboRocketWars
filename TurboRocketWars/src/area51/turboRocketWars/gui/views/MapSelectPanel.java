package area51.turboRocketWars.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import area51.turboRocketWars.Bodies.maps.Map;


public class MapSelectPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image background;

	private final static int TITLE_CONTAINER_HEIGHT = 150;

	private final static Color BUTTON_FONT_COLOR_NO_FOCUS = Color.red;
	private final static Color BUTTON_FONT_COLOR_FOCUS = Color.white;

	public final static String TITLE_LABEL = "SELECT MAP";
	public final static String SELECTED_MAP_TITLE = "Selected map: ";

	private static int LABEL_FONT_SIZE = 50;
	private static int LABEL_FONT_STYLE = Font.BOLD+Font.ITALIC;
	private static String LABEL_FONT_NAME = Font.SERIF;
	
	private final static int PREVIEW_PANEL_OFFSET_X = 300;
	private final static int PREVIEW_PANEL_OFFSET_Y = 0;

	private final static int BUTTON_WIDTH = 100;
	private final static int BUTTON_HEIGHT = 70;

	public final static String RETURN_ACTION = "Return";
	
	private static int LABEL_MARGIN_LEFT = 0;
	private static int LABEL_MARGIN_RIGHT = 0;
	private static int LABEL_MARGIN_TOP = 50;
	private static int LABEL_MARGIN_BOTTOM = 90;

	private static int BUTTON_FONT_SIZE = 20;
	private static int BUTTON_FONT_STYLE = Font.BOLD;
	private static String BUTTON_FONT_NAME = Font.SANS_SERIF;

	private final static int PREVIEW_WIDTH = 600;
	private final static int PREVIEW_HEIGHT = 300;

	private JTextArea lblSelectedMapValue;

	private JButton btnReturn;
	private JButton[] buttons;
	private JButton selectedButton;
	private int selectedIndex = 0;

	private ActionListener buttonListener;

	private JPanel titleContainer;
	private JPanel contentContainer;
	private JPanel previewContainer;
	private MainGamePanel previewMapPanel;
	private JPanel previewInfoPanel;
	private JPanel optionsPanel;

	private Map[] maps;
	private boolean initialized = false;

	public MapSelectPanel(ActionListener buttonListener, Map[] maps){
		this.maps = maps;
		this.buttonListener = buttonListener;
	}

	private void createPreviewPanel(Map[] maps) {
		previewContainer = new JPanel();
		previewContainer.setLayout(null);
		previewContainer.setBackground(Color.blue);
		System.out.println("A/B=" + PREVIEW_WIDTH + "/" + maps[0].getWidth() + "=" + PREVIEW_WIDTH/maps[0].getWidth());
		previewMapPanel = new MainGamePanel(null, null, new OBBViewportTransform(), PREVIEW_WIDTH/maps[0].getWidth(), maps[0].getWidth()/2, maps[0].getHeight()/2);	
		previewInfoPanel = new JPanel();
		previewInfoPanel.setLayout(new BoxLayout(previewInfoPanel, BoxLayout.X_AXIS));

		lblSelectedMapValue = new JTextArea();
		lblSelectedMapValue.setFocusable(false);
		lblSelectedMapValue.setWrapStyleWord(true);
		lblSelectedMapValue.setLineWrap(true);
		lblSelectedMapValue.setOpaque(false);
		
		lblSelectedMapValue.setForeground(BUTTON_FONT_COLOR_NO_FOCUS);
		lblSelectedMapValue.setAlignmentX(LEFT_ALIGNMENT);
		lblSelectedMapValue.setFont(new Font(BUTTON_FONT_NAME, BUTTON_FONT_STYLE, BUTTON_FONT_SIZE));
		lblSelectedMapValue.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);

		previewContainer.add(previewMapPanel);
		previewInfoPanel.add(lblSelectedMapValue);
		previewContainer.add(previewInfoPanel);
	}

	private void createOptionsPanel(Map[] maps) {
		optionsPanel = new JPanel();	
		optionsPanel.setAlignmentY(TOP_ALIGNMENT);
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.setBackground(Color.green);

		btnReturn = new JButton("Return");
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
	}

	public void createTitlePanel(){
		JLabel lblTitle = new JLabel(TITLE_LABEL);
		lblTitle.setFont(new Font(LABEL_FONT_NAME, LABEL_FONT_STYLE, LABEL_FONT_SIZE));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);

		Border lblPadding = new EmptyBorder(LABEL_MARGIN_TOP, LABEL_MARGIN_LEFT, LABEL_MARGIN_BOTTOM, LABEL_MARGIN_RIGHT);
		lblTitle.setBorder(lblPadding);
		
		
		titleContainer = new JPanel();

		titleContainer.add(lblTitle);
		titleContainer.setBackground(Color.red); // for test
	}

	public void createContentPanel(){
		contentContainer = new JPanel();
		contentContainer.setLayout(null);
		contentContainer.setAlignmentY(TOP_ALIGNMENT);
		contentContainer.setBackground(Color.yellow); // for test
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {		
		if(initialized) return;
		super.setBounds(x, y, width, height);
		System.out.println("initializing...");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		createTitlePanel();	
		createContentPanel();
		createOptionsPanel(maps);	
		createPreviewPanel(maps);

		optionsPanel.setPreferredSize(new Dimension(width/3, height-TITLE_CONTAINER_HEIGHT));

		previewMapPanel.setBounds(PREVIEW_PANEL_OFFSET_X, PREVIEW_PANEL_OFFSET_Y, PREVIEW_WIDTH, PREVIEW_HEIGHT);
		previewInfoPanel.setBounds(PREVIEW_PANEL_OFFSET_X, PREVIEW_HEIGHT + PREVIEW_PANEL_OFFSET_Y + 20, PREVIEW_WIDTH, PREVIEW_HEIGHT);
		optionsPanel.setBounds(0, TITLE_CONTAINER_HEIGHT, width/2, height-TITLE_CONTAINER_HEIGHT);
		previewContainer.setBounds(width/2, TITLE_CONTAINER_HEIGHT, width/2, height-TITLE_CONTAINER_HEIGHT);
		
		titleContainer.setMaximumSize(new Dimension(width, TITLE_CONTAINER_HEIGHT));
		
//		previewContainer.setMaximumSize(new Dimension(PREVIEW_WIDTH*2, height-TITLE_CONTAINER_HEIGHT));
//		previewMapPanel.setMaximumSize(new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT));
//		previewInfoPanel.setMaximumSize(new Dimension(PREVIEW_WIDTH, height-PREVIEW_HEIGHT-TITLE_CONTAINER_HEIGHT));
		
		

		contentContainer.add(optionsPanel);
//		contentContainer.add(Box.createHorizontalStrut(width/3-10));
		contentContainer.add(previewContainer);
		add(titleContainer, BorderLayout.NORTH);
		add(contentContainer, BorderLayout.CENTER);

		setVisible(true);
		selectMap(maps[0]);

		this.setOpaque(false);
		contentContainer.setOpaque(false);
		optionsPanel.setOpaque(false);
		previewContainer.setOpaque(false);
		titleContainer.setOpaque(false);
		previewInfoPanel.setOpaque(false);
		previewMapPanel.setOpaque(false);

		initialized = true;
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
		if(selectedIndex != buttons.length-1 && selectedIndex < buttons.length) showMap(maps[selectedIndex]);
	}
	public void focusPrevButton(){
		selectButton(buttons[(selectedIndex=((selectedIndex == 0 ? buttons.length : selectedIndex) -1)%buttons.length)]);
		if(selectedIndex != buttons.length-1 && selectedIndex < buttons.length) showMap(maps[selectedIndex]); 
	}

	private void setButtonStylesAndListener(JButton c){
		c.setForeground(BUTTON_FONT_COLOR_NO_FOCUS);
		c.setAlignmentX(RIGHT_ALIGNMENT);
		c.setAlignmentY(TOP_ALIGNMENT);
		c.setContentAreaFilled(false);
		c.setEnabled(false);
		c.setBorderPainted(false);
		c.setFont(new Font(BUTTON_FONT_NAME, BUTTON_FONT_STYLE, BUTTON_FONT_SIZE));

//		c.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
		c.addActionListener(buttonListener);
		optionsPanel.add(c);
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
	
	private void showMap(Map m){
		m.create(new World(new Vec2())).preview(previewMapPanel, 100, 100, true);
	}

	public void selectMap(Map m) {
		showMap(m);
		lblSelectedMapValue.setText(SELECTED_MAP_TITLE + m.getName());	
	}

}
