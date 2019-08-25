package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import application.MSQP;

public class MSQPview extends JFrame {

	private static final long serialVersionUID = 2772932155434775875L;
	private static final String APP_NAME = "Mark Six Quick Pick";
	private static final String[] COLUMN_NAMES = { "Entry #", "Combination" };
	private static final DecimalFormat DF = new DecimalFormat("###,###,##0.00");

	private Dimension myFrameSize;

	private ButtonGroup myEntryBtnGp;
	private JRadioButton mySingle;
	private JRadioButton myMultiple;

	private ButtonGroup myInvestBtnGp;
	private JRadioButton myUnit;
	private JRadioButton myPartial;

	private ButtonGroup myFontBtnGp;
	private JRadioButton mySmall;
	private JRadioButton myMedium;
	private JRadioButton myLarge;

	private JButton myGenBtn;
	private JButton myQuitBtn;

	private JMenuItem myGenItem;
	private JMenuItem myQuitItem;
	private JMenuItem myAboutItem;

	private DefaultTableModel myTableModel;
	private JTable myTable;
	private JScrollPane myScrollPane;

	private JComboBox<Integer> myTotalSelections;
	private JComboBox<Integer> myNumOfEntries;

	private JLabel myInvestment;
	private int myAmount;
	private int myEntryColumnWidth;
	private String[][] myData;

	public MSQPview() {
		super(APP_NAME);
		myFrameSize = MSQP.getOS().startsWith("mac") ? new Dimension(900, 550) : new Dimension(900, 550);
		myEntryBtnGp = new ButtonGroup();
		myInvestBtnGp = new ButtonGroup();
		myFontBtnGp = new ButtonGroup();
		myTable = new JTable() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		myScrollPane = new JScrollPane(myTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		myTotalSelections = new JComboBox<>();
		myNumOfEntries = new JComboBox<>();
		myAmount = 0;
		myEntryColumnWidth = 49;
		myInvestment = new JLabel("Investment per entry: $" + DF.format(myAmount));
		setPreferredSize(myFrameSize);
		setMinimumSize(myFrameSize);
		initComponents();
	}

	private void initComponents() {
		buildEntryRadio();
		buildInvestRadio();
		buildFontRadio();
		buildButtons();
		buildMenuItems();
		buildComboBox();
		radioBtnSwitch(false);
	}

	public void createAndShowGUI() {
		setUpComponents();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void setUpComponents() {
		setJMenuBar(createMenuBar());
		setContentPane(createContentPane());
	}

	private void buildEntryRadio() {
		mySingle = new JRadioButton("Single");
		myMultiple = new JRadioButton("Multiple");
		mySingle.setSelected(true);

		myEntryBtnGp.add(mySingle);
		myEntryBtnGp.add(myMultiple);

		myMultiple.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					myTotalSelections.removeItemAt(0);
					myTotalSelections.setEnabled(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					myTotalSelections.insertItemAt(6, 0);
					myTotalSelections.setSelectedIndex(0);
					myTotalSelections.setEnabled(false);
				}
			}
		});
	}

	private void buildInvestRadio() {
		myUnit = new JRadioButton("Unit Bet $10");
		myPartial = new JRadioButton("Partial Bet $5");
		myUnit.setSelected(true);

		myInvestBtnGp.add(myPartial);
		myInvestBtnGp.add(myUnit);

		myUnit.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					myAmount = myAmount * 2;
					myInvestment.setText("Investment per entry: $" + DF.format(myAmount));
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					myAmount = myAmount / 2;
					myInvestment.setText("Investment per entry: $" + DF.format(myAmount));
				}
			}
		});
	}

	private void buildFontRadio() {
		mySmall = new JRadioButton("Small");
		myMedium = new JRadioButton("Medium");
		myLarge = new JRadioButton("Large");
		myMedium.setSelected(true);

		myFontBtnGp.add(mySmall);
		myFontBtnGp.add(myMedium);
		myFontBtnGp.add(myLarge);

		mySmall.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					configTableFontSize(10, 10);
					myEntryColumnWidth = 41;
					setEntryColumnWidth(myEntryColumnWidth);
				}
			}
		});

		myMedium.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					configTableFontSize(13, 13);
					myEntryColumnWidth = 49;
					setEntryColumnWidth(myEntryColumnWidth);
				}
			}
		});

		myLarge.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					configTableFontSize(14, 14);
					myEntryColumnWidth = 54;
					setEntryColumnWidth(myEntryColumnWidth);
				}
			}
		});
	}

	private void buildButtons() {
		myGenBtn = new JButton("Generate");
		myGenBtn.setPreferredSize(new Dimension(300, 40));
		myGenBtn.setToolTipText("Generate the combination(s).");
		myGenBtn.setFocusable(false);

		myQuitBtn = new JButton("Exit");
		myQuitBtn.setPreferredSize(new Dimension(80, 40));
		myQuitBtn.setToolTipText("Close the program.");
		myQuitBtn.setFocusable(false);
		myQuitBtn.addActionListener(theEvent -> exitProgram());
	}

	private void buildMenuItems() {
		myGenItem = new JMenuItem("Generate");
		myGenItem.setToolTipText("Generate the combination(s).");

		myQuitItem = new JMenuItem("Exit");
		myQuitItem.setToolTipText("Close the program.");
		myQuitItem.addActionListener(theEvent -> exitProgram());

		myAboutItem = new JMenuItem("About...");
		myAboutItem.setToolTipText("About Mark Six Quick Pick...");
	}

	private void buildComboBox() {
		for (int i = 6; i <= 49; i++) {
			myTotalSelections.addItem(i);
		}

		for (int i = 1; i <= 100; i++) {
			myNumOfEntries.addItem(i);
		}

		myTotalSelections.setEnabled(false);
		myNumOfEntries.setSelectedIndex(19);
	}

	private JMenuBar createMenuBar() {
		JMenuBar bar = new JMenuBar();
		bar.add(this.createFileMenu());
		bar.add(this.createHelpMenu());
		return bar;
	}

	private JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);

		menu.add(myGenItem);
		menu.add(new JSeparator());
		menu.add(myQuitItem);

		return menu;
	}

	private JMenu createHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		menu.add(myAboutItem);
		return menu;
	}

	private JPanel createContentPane() {
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(myFrameSize);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		contentPane.add(northPanel(), BorderLayout.NORTH);
		contentPane.add(mainPanel(), BorderLayout.CENTER);
		contentPane.add(southPanel(), BorderLayout.SOUTH);
		return contentPane;
	}

	private JPanel northPanel() {
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());

		JLabel title = new JLabel("Welcome to " + APP_NAME + "!");
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setFont(new Font("Tahoma", Font.PLAIN, 18));
		northPanel.add(title, BorderLayout.WEST);
		northPanel.add(new JSeparator(), BorderLayout.SOUTH);
		northPanel.add(myInvestment, BorderLayout.EAST);

		return northPanel;
	}

	private JPanel mainPanel() {
		JPanel main = new JPanel();
		main.setLayout(new BorderLayout());

		JPanel west = new JPanel();
		west.setLayout(new BoxLayout(west, BoxLayout.X_AXIS));
		west.add(westPanel());
		west.add(new JSeparator(SwingConstants.VERTICAL));

		main.add(west, BorderLayout.WEST);
		main.add(outputPanel(), BorderLayout.CENTER);

		return main;
	}

	private JPanel southPanel() {
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());

		southPanel.add(myGenBtn, BorderLayout.CENTER);
		southPanel.add(myQuitBtn, BorderLayout.EAST);
		southPanel.add(new JSeparator(), BorderLayout.NORTH);

		return southPanel;
	}

	private JPanel westPanel() {
		JPanel westPanel = new JPanel();

		JPanel options = new JPanel();
		options.setLayout(new GridLayout(100, 1));

		JLabel setting = new JLabel("<html><u>Settings</u></html>");
		setting.setHorizontalAlignment(SwingConstants.CENTER);
		setting.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
		setting.setFont(new Font("Tahoma", Font.BOLD, 15));
		options.add(setting);

		options.add(createSubHeader("Type of Entry:"));
		options.add(mySingle);
		options.add(myMultiple);
		options.add(createSubHeader("Total Selections:"));
		options.add(myTotalSelections);
		options.add(createSubHeader("Num. of Entries:"));
		options.add(myNumOfEntries);

		options.add(createSubHeader("Investment:"));
		options.add(myUnit);
		options.add(myPartial);

		options.add(createSubHeader("Output Font Size:"));
		options.add(mySmall);
		options.add(myMedium);
		options.add(myLarge);

		westPanel.add(options);

		return westPanel;
	}

	private JPanel outputPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		configTableFontSize(13, 13);
		myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		panel.add(myScrollPane, BorderLayout.CENTER);
		return panel;
	}

	private void exitProgram() {
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	private void updateTableModel() {
		myTableModel = new DefaultTableModel(myData, COLUMN_NAMES);
		myTable.setModel(myTableModel);
		for (int column = 0; column < myTable.getColumnCount(); column++) {
			TableColumn tableColumn = myTable.getColumnModel().getColumn(column);
			int preferredWidth = tableColumn.getMinWidth();
			int maxWidth = tableColumn.getMaxWidth();

			for (int row = 0; row < myTable.getRowCount(); row++) {
				TableCellRenderer cellRenderer = myTable.getCellRenderer(row, column);
				Component c = myTable.prepareRenderer(cellRenderer, row, column);
				int width = c.getPreferredSize().width + myTable.getIntercellSpacing().width;
				preferredWidth = Math.max(preferredWidth, width);

				// We've exceeded the maximum width, no need to check other rows
				if (preferredWidth >= maxWidth) {
					preferredWidth = maxWidth;
					break;
				}
			}

			tableColumn.setMinWidth(preferredWidth + 10);
			tableColumn.setPreferredWidth(preferredWidth + 10);
		}
	}

	private void configTableFontSize(int theHeaderSize, int theTextSize) {
		myTable.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, theHeaderSize));
		myTable.setFont(new Font("Tahoma", Font.PLAIN, theTextSize));
		if (myTableModel != null) {
			updateTableModel();
		}
	}

	private void setEntryColumnWidth(int theWidth) {
		myTable.getColumnModel().getColumn(0).setMinWidth(theWidth);
		myTable.getColumnModel().getColumn(0).setPreferredWidth(theWidth);
	}

	private void radioBtnSwitch(boolean theBool) {
		myUnit.setEnabled(theBool);
		myPartial.setEnabled(theBool);
		mySmall.setEnabled(theBool);
		myMedium.setEnabled(theBool);
		myLarge.setEnabled(theBool);
	}

	private JLabel createSubHeader(String theText) {
		JLabel label = new JLabel(theText);
		label.setFont(new Font("Tahoma", Font.BOLD, 13));

		return label;
	}

	public void updateData(String[][] theData, int theInvestment) {
		radioBtnSwitch(true);
		myData = theData;
		myAmount = theInvestment;
		myInvestment.setText("Investment per entry: $" + DF.format(myAmount));

		updateTableModel();
		setEntryColumnWidth(myEntryColumnWidth);
	}

	public int getTotalSelections() {
		return (int) myTotalSelections.getSelectedItem();
	}

	public int getNumOfEntries() {
		return (int) myNumOfEntries.getSelectedItem();
	}

	public char getInvestmentType() {
		return myUnit.isSelected() ? 'U' : 'P';
	}

	public void addGenerateActionListener(ActionListener theAction) {
		myGenBtn.addActionListener(theAction);
		myGenItem.addActionListener(theAction);
	}

}
