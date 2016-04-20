package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;

public class Frame1 {

	private JFrame mainFrame;

	/**
	 * Launch the application.
	 */
	public void mainFrame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("Software Engineering Processes Assignment #1");
		mainFrame.setBounds(100, 100, 1000, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);

		// Weather stations label
		JLabel lblWeatherStations = new JLabel("Weather stations");
		lblWeatherStations.setBounds(431, 38, 117, 14);
		mainFrame.getContentPane().add(lblWeatherStations);

		// Refresh button
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setBounds(815, 34, 89, 23);
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//control.refreshcall()
			}
		});
		mainFrame.getContentPane().add(btnRefresh);

		// List of states label
		JLabel lblListOfStates = new JLabel("List of States");
		lblListOfStates.setBounds(89, 80, 89, 23);
		mainFrame.getContentPane().add(lblListOfStates);

		// Favourites panel
		JScrollPane paneFavourites = new JScrollPane();
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
		JButton btnAddToFavourites = new JButton("Add to favourites");
		btnAddToFavourites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//control.addToFav();
			}
		});
		btnAddToFavourites.setBounds(402, 487, 157, 23);
		mainFrame.getContentPane().add(btnAddToFavourites);

		// Split pane: Left side = state, right = data for state
		JSplitPane splitPane = new JSplitPane();
		// Lets you adjust ratio of the two panes.
		splitPane.setResizeWeight(0.2);
		splitPane.setOneTouchExpandable(true);
		splitPane.setContinuousLayout(true);
		splitPane.setBounds(63, 105, 571, 369);
		mainFrame.getContentPane().add(splitPane);

		// Scroll pane for the list of states (left)
		JScrollPane paneStates = new JScrollPane();
		splitPane.setLeftComponent(paneStates);

		// Scroll pane for the data (right)
		JScrollPane paneData = new JScrollPane();
		splitPane.setRightComponent(paneData);
	}
}
