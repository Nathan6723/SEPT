package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class View
{
	private JButton btnRefresh = new JButton("Refresh");
	private JButton btnAddToFavourites = new JButton("Add to favourites");
	private JButton btnRemoveFromFavourites = new JButton("Remove from favourites");
	private JTree statesTree = new JTree(new DefaultMutableTreeNode(""));
	private JTree favouritesTree = new JTree(new DefaultMutableTreeNode(""));
	private JComboBox<String> monthsCombo;
	private JTable table = new JTable();
	private JButton btnProduceGraph;
	private JLabel lblTableOfData = new JLabel(WINDOW_LABEL);
	private JFrame frame;
	
	public final static String WINDOW_LABEL = "Weather data";
	public final static String WINDOW_NAME = "Australian Weather Reporter";
	
	public View()
	{
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBorder(new EmptyBorder(5, 25, 10, 50));
		
		// List of states label
		JLabel lblListOfStates = new JLabel("States and stations");
		topPanel.add(lblListOfStates, BorderLayout.WEST);

		// Favourites label
		JLabel lblFavourites = new JLabel("Favourites");
		topPanel.add(lblFavourites, BorderLayout.EAST);

		// Table data label
		lblTableOfData = new JLabel("Weather data");
		lblTableOfData.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(lblTableOfData, BorderLayout.CENTER);
		
		mainPanel.add(topPanel, BorderLayout.NORTH);
		
		favouritesTree.setRootVisible(false);
		favouritesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		statesTree.setRootVisible(false);
		statesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		JPanel centerPanel = new JPanel(new BorderLayout());
		
		// Scroll pane for the list of states
		JScrollPane paneStates = new JScrollPane(statesTree);
		paneStates.setPreferredSize(new Dimension(180, 0));
		centerPanel.add(paneStates, BorderLayout.WEST);
		
		JPanel centerBottomPanel = new JPanel(new BorderLayout());
		
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setDefaultEditor(Object.class, null);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setRowHeight(25);
		table.setFillsViewportHeight(true);
		
		// Scroll pane for the data
		JScrollPane paneData = new JScrollPane(table);
		paneData.setPreferredSize(new Dimension(0, 588));
		centerBottomPanel.add(paneData, BorderLayout.NORTH);
		
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel bottomPanel = new JPanel(new GridBagLayout());
		bottomPanel.setBorder(new EmptyBorder(25, 100, 20, 100));
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		monthsCombo = new JComboBox<>();
		monthsCombo.setMaximumRowCount(4);
		monthsCombo.setPreferredSize(new Dimension(150, 30));
		monthsCombo.setVisible(false);
		Calendar calendar = Calendar.getInstance();
		String[] months = {"January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November", "December"};
		for (int i = 0; i < 6; ++i)
		{
			int month = calendar.get(Calendar.MONTH) - i;
			if (month < 0)
				month = 12 + month;
			monthsCombo.addItem(months[month]);
		}
		bottomPanel.add(monthsCombo, gbc);
		
		// Button to produce graph window for data
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		btnProduceGraph = new JButton("Produce Graph");
		btnProduceGraph.setPreferredSize(new Dimension(150, 30));
		btnProduceGraph.setVisible(false);
		bottomPanel.add(btnProduceGraph, gbc);

		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		btnAddToFavourites.setPreferredSize(new Dimension(150, 30));
		btnAddToFavourites.setVisible(false);
		bottomPanel.add(btnAddToFavourites, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		btnRemoveFromFavourites.setPreferredSize(new Dimension(150, 30));
		btnRemoveFromFavourites.setVisible(false);
		bottomPanel.add(btnRemoveFromFavourites, gbc);
		
		// Refresh button
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.SOUTH;
		btnRefresh.setPreferredSize(new Dimension(150, 30));
		bottomPanel.add(btnRefresh, gbc);
		
		centerBottomPanel.add(bottomPanel, BorderLayout.CENTER);
		
		centerPanel.add(centerBottomPanel, BorderLayout.CENTER);
		
		// Favourites panel
		JScrollPane paneFavourites = new JScrollPane(favouritesTree);
		paneFavourites.setPreferredSize(new Dimension(180, 0));
		centerPanel.add(paneFavourites, BorderLayout.EAST);
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);

		frame = new JFrame();
		frame.setTitle(WINDOW_NAME);
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 800));
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setResizable(false);
        frame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public JLabel getLblTableOfData()
	{
		return lblTableOfData;
	}
	
	public JComboBox<String> getMonthsCombo()
	{
		return monthsCombo;
	}
	
	public JButton getBtnProduceGraph()
	{
		return btnProduceGraph;
	}
	
	public JTable getJTable()
	{
		return table;
	}
	
	public JTree getStatesJTree()
	{
		return statesTree;
	}
	
	public JTree getFavouritesJTree()
	{
		return favouritesTree;
	}
	
	public JButton getBtnRefresh()
	{
		return btnRefresh;
	}
	
	public JButton getBtnAddToFavourites()
	{
		return btnAddToFavourites;
	}
	
	public JButton getBtnRemoveFromFavourites()
	{
		return btnRemoveFromFavourites;
	}
}
