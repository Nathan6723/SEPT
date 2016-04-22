package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class Graph {

	private JFrame graph;

	/**
	 * Launch the application.
	 */
	public static void GraphWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Graph window = new Graph();
					window.graph.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Graph() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		graph = new JFrame();
		graph.setTitle("Graph of data");
		graph.setBounds(100, 100, 450, 300);
		graph.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		graph.getContentPane().setLayout(null);
	}

}

