package area51.turboRocketWars.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

	private final static int TITLE_CONTAINER_HEIGHT = 150;

	private final static Color BUTTON_FONT_COLOR_NO_FOCUS = Color.red;
	private final static Color BUTTON_FONT_COLOR_FOCUS = Color.white;

	public final static String TITLE_LABEL = "SELECT MAP";
	public final static String SELECTED_MAP_TITLE = "Selected map";

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


	private MainGamePanel previewMapPanel;

	private boolean initialized = false;
	GridBagConstraints c = new GridBagConstraints();

	private static final int lblGridx = 0;
	private static final int lblGridy = 0;
	private static final int lblGridWidth = 10;
	private static final double lblWeightx = 0;
	private static final double lblWeighty = 0;
	private static final int lblFill = GridBagConstraints.NONE;
	private static final int lblAnchor = GridBagConstraints.CENTER;

	private static final int btnGridx = 1;
	private static final int btnGridy = 1;
	private static final int btnGridWidth = 3;
	private static final double btnWeightx = 0.5;
	private static final double btnWeighty = 0;
	private static final int btnFill = GridBagConstraints.NONE;
	private static final int btnAnchor = GridBagConstraints.LINE_END;

	private static final int paneGridx = 5;
	private static final int paneGridy = 1;
	private static final int paneGridWidth = 3;
	private static final double paneWeightx = 0.5;
	private static final double paneWeighty = 0.8;
	private static final int paneFill = GridBagConstraints.NONE;
	private static final int paneAnchor = GridBagConstraints.FIRST_LINE_START;

	private static final double mapInfoWeight = 0.2;

	private final String informationText = "Map information"
			+ "\n%1$s"
			+ "\n\n%2$s";
	private final String currentMapInfo = "Current map: %1$s "
			+ "\nNumber of spawn points: %2$d";
	private final String selectedMapInfo = "Selected map: %1$s"
			+ "\nNumber of spawn points: %2$d";

	private Map selectedMap;
	private Map currentMap;

	public MapSelectPanel(ActionListener buttonListener, String[] maps){
		this.buttonListener = buttonListener;

		setLayout(new GridBagLayout());	
		setOpaque(false);
		// adding title label
		JLabel lblTitle = new JLabel(TITLE_LABEL);
		lblTitle.setFont(new Font(LABEL_FONT_NAME, LABEL_FONT_STYLE, LABEL_FONT_SIZE));
		lblTitle.setAlignmentX(CENTER_ALIGNMENT);
		Border lblPadding = new EmptyBorder(LABEL_MARGIN_TOP, LABEL_MARGIN_LEFT, LABEL_MARGIN_BOTTOM, LABEL_MARGIN_RIGHT);
		lblTitle.setBorder(lblPadding);

		c.gridx = lblGridx;
		c.gridy = lblGridy;
		c.gridwidth = lblGridWidth;
		c.weighty = lblWeighty;
		c.weightx = lblWeightx;
		c.fill = lblFill;
		c.anchor = lblAnchor;
		add(lblTitle, c);

		// adding buttons and maps. buttons are added from top to bottom
		buttons = new JButton[maps.length*5 +1];
		if(maps != null){
			for(int i = 0; i < maps.length*5; i++){
				JButton b = new JButton(maps[i%maps.length]);
				b.setActionCommand(maps[i%maps.length]);
				setButtonStylesAndListener(b, i+1);
				buttons[i] = b;
			}
		}
		btnReturn = new JButton("Return");
		setButtonStylesAndListener(btnReturn, buttons.length);
		buttons[buttons == null ? 0 : buttons.length-1] = btnReturn;

		btnReturn.setActionCommand(RETURN_ACTION);
		selectedIndex = buttons.length-1;		
		selectButton(btnReturn); // set return button as highlighted button

		// setting up preview view. values are not import in MainGamePanel constructor. Are being set when world is being set
		previewMapPanel = new MainGamePanel(null, null, new OBBViewportTransform(), 0, 0, 0);
		previewMapPanel.setOpaque(false);
		previewMapPanel.setPreferredSize(new Dimension(PREVIEW_WIDTH, PREVIEW_HEIGHT));		

		c.gridx = paneGridx;
		c.gridy = paneGridy;
		c.gridheight = buttons.length;
		c.gridwidth = paneGridWidth;
		c.weightx = paneWeightx;
		c.weighty = 0.1;//paneWeighty;
		c.fill = paneFill;
		c.anchor = paneAnchor;
		add(previewMapPanel, c);

		// setting information area
		lblSelectedMapValue = new JTextArea();
		lblSelectedMapValue.setEnabled(false);
		lblSelectedMapValue.setFocusable(false);
		lblSelectedMapValue.setForeground(Color.red);
		lblSelectedMapValue.setDisabledTextColor(Color.red);
		lblSelectedMapValue.setFont(new Font(Font.SERIF, Font.BOLD, BUTTON_FONT_SIZE));
		lblSelectedMapValue.setPreferredSize(new Dimension(PREVIEW_WIDTH, 250));
		lblSelectedMapValue.setLineWrap(true);
		lblSelectedMapValue.setOpaque(false);

		c.gridx = paneGridx;
		c.gridwidth = paneGridWidth;
		c.gridy = paneGridy + buttons.length + 1;
		c.weighty = 0.9;
		c.fill = paneFill;
		c.weightx = paneWeightx;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		add(lblSelectedMapValue, c);

	}


	@Override
	public void setBounds(int x, int y, int width, int height) {		
		super.setBounds(x, y, width, height);
		if(initialized) return;
		setVisible(true);
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

	/**
	 * moves focus to next button in button list. makes wrap around as well.
	 * @return the text of focused button. The text corresponds to the actioncommand of the button as well.
	 */
	public String focusNextButton(){
		selectButton(buttons[(selectedIndex=(selectedIndex+1)%buttons.length)]);
		return selectedButton.getText();
	}
	
	/**
	 * moves focus to previous button in button list. makes wrap around as well.
	 * @return the text of focused button. The text corresponds to the actioncommand of the button as well.
	 */
	public String focusPrevButton(){
		selectButton(buttons[(selectedIndex=((selectedIndex == 0 ? buttons.length : selectedIndex) -1)%buttons.length)]);
		return selectedButton.getText();
	}

	private void setButtonStylesAndListener(JButton c, int y){
		c.setForeground(BUTTON_FONT_COLOR_NO_FOCUS);
		c.setContentAreaFilled(false);
		c.setEnabled(false);
		c.setBorderPainted(false);
		c.setFont(new Font(BUTTON_FONT_NAME, BUTTON_FONT_STYLE, BUTTON_FONT_SIZE));
		GridBagConstraints cns = new GridBagConstraints();

		cns.gridx = btnGridx;
		cns.gridy = y;
		cns.gridwidth = btnGridWidth;
		cns.weightx = btnWeightx;
		cns.weighty = btnWeighty;
		cns.anchor = btnAnchor;
		cns.fill = btnFill;

		c.addActionListener(buttonListener);
		add(c, cns);
	}

	public JPanel getPanel(){
		return this;
	}

	/**
	 * updates map shown in preview. Information for map is updated as well.
	 * @param m the map a preview should be shown for.
	 */
	public void showMap(Map m){
		if(m == null) return;
		m.create(new World(new Vec2())).preview(previewMapPanel, PREVIEW_WIDTH, PREVIEW_HEIGHT, true);		
		updateInformationText(m, null);
	}

	/**
	 * updates the map shown in preview and information about selected map, the map that will be used when game starts.
	 * @param m the map to show and get information from.
	 */
	public void selectMap(Map m) {
		showMap(m);
		updateInformationText(null, m);	
	}

	private void updateInformationText(Map current, Map selected){
		if(current != null) currentMap = current;
		if(selected != null) selectedMap = selected;
		String infoCurrent = String.format(currentMapInfo, currentMap == null ? "" : currentMap.getName(), currentMap == null ? -1 : currentMap.getNumSpawnPoints());
		String infoSelected = String.format(selectedMapInfo, selectedMap == null ? "" : selectedMap.getName(), selectedMap == null ? -1 : selectedMap.getNumSpawnPoints());
		lblSelectedMapValue.setText(String.format(informationText, infoCurrent, infoSelected));
	}

}
