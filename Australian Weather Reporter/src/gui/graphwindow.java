package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class graphwindow {

	private JFrame graph;

	/**
	 * Launch the application.
	 */
	public static void GraphWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					graphwindow window = new graphwindow();
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
	public graphwindow() {
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

