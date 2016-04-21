package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class View
{
	private JFrame mainFrame;
	private JButton btnRefresh = new JButton("Refresh");
	private JButton btnAddToFavourites = new JButton("Add to favourites");
	private JButton btnRemoveFromFavourites = new JButton("Remove from favourites");
	private JTree statesTree = new JTree(new DefaultMutableTreeNode(""));
	private JTree favouritesTree = new JTree(new DefaultMutableTreeNode(""));
	private JTable table = new JTable();
	
	private final static String WINDOW_NAME = "Australian Weather Reporter";

	/**
	 * Create the application.
	 */
	public View()
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				initialize();
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		mainFrame = new JFrame();
		mainFrame.setTitle(WINDOW_NAME);
		mainFrame.setBounds(100, 100, 1000, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(true);
		
		// Weather stations label
		JLabel lblWeatherStations = new JLabel("Weather stations");
		lblWeatherStations.setBounds(431, 38, 117, 14);
		mainFrame.getContentPane().add(lblWeatherStations);
		
		// Refresh button
		btnRefresh.setBounds(815, 34, 89, 23);
		
		mainFrame.getContentPane().add(btnRefresh);
		
		// List of states label
		JLabel lblListOfStates = new JLabel("List of States");
		lblListOfStates.setBounds(89, 80, 89, 23);
		mainFrame.getContentPane().add(lblListOfStates);

		favouritesTree.setRootVisible(false);
		
		// Favourites panel
		JScrollPane paneFavourites = new JScrollPane(favouritesTree);
		paneFavourites.setBounds(702, 100, 207, 398);
		mainFrame.getContentPane().add(paneFavourites);

		// Favourites label
		JLabel lblFavourites = new JLabel("Favourites");
		lblFavourites.setBounds(773, 84, 89, 14);
		mainFrame.getContentPane().add(lblFavourites);

		// Table data label
		JLabel lblTableOfData = new JLabel("Table of data");
		lblTableOfData.setBounds(374, 84, 117, 14);
		mainFrame.getContentPane().add(lblTableOfData);

		// Button to produce graph window for data
		JButton btnProduceGraph = new JButton("Produce Graph");
		btnProduceGraph.addActionListener(new ActionListener() {
			// Opens the new window with graph when button is clicked (refer to class 'graphwindow')
			public void actionPerformed(ActionEvent e) {
				graphwindow newwindow = new graphwindow();
				newwindow.GraphWindow();
			}
		});
		
		btnProduceGraph.setBounds(253, 487, 139, 23);
		mainFrame.getContentPane().add(btnProduceGraph);

		// Add to favourites button
		btnAddToFavourites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//control.addToFav();
			}
		});
		
		btnAddToFavourites.setBounds(402, 487, 157, 23);
		mainFrame.getContentPane().add(btnAddToFavourites);
		btnAddToFavourites.setVisible(false);
		
		btnRemoveFromFavourites.setBounds(402, 487, 157, 23);
		mainFrame.getContentPane().add(btnRemoveFromFavourites);
		btnRemoveFromFavourites.setVisible(false);

		// Split pane: Left side = state, right = data for state
		JSplitPane splitPane = new JSplitPane();
		// Lets you adjust ratio of the two panes.
		splitPane.setResizeWeight(0.2);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setBounds(63, 105, 571, 369);
		mainFrame.getContentPane().add(splitPane);
		
		statesTree.setRootVisible(false);
		
		// Scroll pane for the list of states (left)
		JScrollPane paneStates = new JScrollPane(statesTree);
		splitPane.setLeftComponent(paneStates);

		// Scroll pane for the data (right)
		JScrollPane paneData = new JScrollPane(table);
		splitPane.setRightComponent(paneData);
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
