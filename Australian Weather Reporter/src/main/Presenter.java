package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import data.Backup;
import data.Data;
import data.State;
import data.Station;
import gui.View;

public class Presenter implements ActionListener, TreeSelectionListener, WindowListener
{
	private Data data = new Data();
	private View view = new View();
	private Backup backup = new Backup();
	
	private int currentMonth;
	private Station currentStation;
	private TreePath lastPath;
	
	private final static Object[] COLUMN_NAMES = new Object[]{"Day", "Min (°C)", "Max (°C)", "Rain (mm)",
			"Evap (mm)", "Sun (hours)", "Dir", "Spd (km/h)", "Time (Local)", "Temp (°C)", "RH (%)", "Cld",
			"Dir", "Spd (km/h)", "MSLP (hPa)", "Temp (°C)", "RH (%)", "Cld", "Dir", "Spd (km/h)", "MSLP (hPa"};
	
	public Presenter()
	{
		addListeners();
		showBackupStates();
		showBackupFavourites();
		startRefresh();
	}
	
	private void addListeners()
	{
		view.getFrame().addWindowListener(this);
		view.getBtnRefresh().addActionListener(this);
		view.getBtnAddToFavourites().addActionListener(this);
		view.getBtnRemoveFromFavourites().addActionListener(this);
		view.getBtnProduceGraph().addActionListener(this);
		view.getMonthsCombo().addActionListener(this);
		view.getStatesJTree().addTreeSelectionListener(this);
		view.getFavouritesJTree().addTreeSelectionListener(this);
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		// Remove other tree selection
		if (e.getSource().equals(view.getStatesJTree()))
			view.getFavouritesJTree().setSelectionPath(null);
		else
			view.getStatesJTree().setSelectionPath(null);
		// Check if station was clicked on
		if (e.getPath().getPathCount() == 3)
		{
			lastPath = e.getPath();
			String stateName = e.getPath().getPathComponent(1).toString();
			String stationName = e.getPath().getPathComponent(2).toString();
			Station station = currentStation = data.getStation(data.getStates(), stateName, stationName);
			if (updateTable(station, 0))
			{
				Station favouriteStation = data.getStation(data.getFavourites(), stateName, stationName);
				if (favouriteStation != null)
				{
					view.getBtnAddToFavourites().setVisible(false);
					view.getBtnRemoveFromFavourites().setVisible(true);
				}
				else
				{
					view.getBtnRemoveFromFavourites().setVisible(false);
					view.getBtnAddToFavourites().setVisible(true);
				}
				view.getBtnProduceGraph().setVisible(true);
				view.getMonthsCombo().setVisible(true);
			}
		}
	}
	
	private boolean updateTable(Station station, int month)
	{
		currentMonth = month;
		ArrayList<ArrayList<String>> rows = data.getDWOStationData(station, month);
		// Somtimes data isn't available
		// Need to display a message
		if (rows == null)
		{
			view.getJTable().setModel(new DefaultTableModel());
			view.getLblTableOfData().setText("No data available");
			return false;
		}
		view.getLblTableOfData().setText(View.WINDOW_LABEL);
		DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
		for (int i = 0; i < rows.size(); ++i)
			model.addRow(rows.get(i).toArray());
		view.getJTable().setModel(model);
		return true;
	}
	
	private void showBackupStates()
	{
		TreeSet<State> states = data.getStatesBackup();
		if (states == null)
			return;
		showTree(view.getStatesJTree(), states);
	}
	
	public void showBackupFavourites()
	{
		TreeSet<State> favourites = data.getFavouritesBackup();
		if (favourites == null)
			return;
		showTree(view.getFavouritesJTree(), favourites);
	}
	
	public void showTree(JTree tree, TreeSet<State> states)
	{
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
		root.removeAllChildren();
		for (State state : states)
		{
			DefaultMutableTreeNode stateNode = new DefaultMutableTreeNode(state.getName());
	        model.insertNodeInto(stateNode, root, root.getChildCount());
			// Add stations to state nodes
			for (Station station : state.getStations())
			{
				DefaultMutableTreeNode stationNode = new DefaultMutableTreeNode(station.getName());
				model.insertNodeInto(stationNode, stateNode, stateNode.getChildCount());
			}
		}
		model.nodeStructureChanged(root);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if (source == view.getBtnRefresh())
			refresh();
		else if (source == view.getBtnAddToFavourites())
			addStationToFavourites();
		else if (source == view.getBtnRemoveFromFavourites())
			removeStationFromFavourites();
		else if (source == view.getBtnProduceGraph())
			produceGraph();
		else if (source == view.getMonthsCombo())
			updateTable(currentStation, view.getMonthsCombo().getSelectedIndex());
	}
	
	private void produceGraph()
	{
		
	}
	
	private void addStationToFavourites()
	{
		if (lastPath == null)
			return;
		String stateName = lastPath.getPathComponent(1).toString();
		String stationName = lastPath.getPathComponent(2).toString();
		Station station = new Station(stationName);
		boolean found = false;
		for (State state : data.getFavourites())
		{
			if (state.getName().equals(stateName))
			{
				state.addStation(station);
				found = true;
				break;
			}
		}
		if (!found)
		{
			State state = new State(stateName);
			state.addStation(station);
			data.getFavourites().add(state);
		}
		// Get all available data for station
		for (int i = 1; i < 6; ++i)
			data.getDWOStationData(data.getStation(data.getStates(), stateName, stationName), i);
		// Update favourites list
		showTree(view.getFavouritesJTree(), data.getFavourites());
		view.getBtnAddToFavourites().setVisible(false);
		view.getBtnRemoveFromFavourites().setVisible(true);
		data.backupFavourites();
	}
	
	private void removeStationFromFavourites()
	{
		if (lastPath == null)
			return;
		String stateName = lastPath.getPathComponent(1).toString();
		String stationName = lastPath.getPathComponent(2).toString();
		loops:
		for (State state : data.getFavourites())
		{
			if (state.getName().equals(stateName))
			{
				for (Station station : state.getStations())
				{
					if (station.getName().equals(stationName))
					{
						state.getStations().remove(station);
						if (state.getStations().size() == 0)
							data.getFavourites().remove(state);
						break loops;
					}
				}
			}
		}
		showTree(view.getFavouritesJTree(), data.getFavourites());
		view.getBtnRemoveFromFavourites().setVisible(false);
		view.getBtnAddToFavourites().setVisible(true);
		data.backupFavourites();
	}
	
	// Refresh program every 30 minutes
	private void startRefresh()
	{
		new Thread(new Runnable()
    	{
    		public void run()
    		{
    			try 
    			{
    				while(true)
    				{
    					refresh();
    					Thread.sleep(1800000);
    				}
    			}
    			catch (InterruptedException e) 
    			{
    				e.printStackTrace();
    			}
    		}
    	}).start();
	}
	
	public void refresh()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!data.isGettingStates())
				{
					String originalText = view.getBtnRefresh().getText();
					view.getBtnRefresh().setText("Refreshing...");
					// Retrieve new state information
					TreeSet<State> updateStates = data.getStatesUpdate();
					// Save expanded nodes
					String expansions = "";
					for (int i = 0 ; i < view.getStatesJTree().getRowCount(); ++i)
					{
				        TreePath path = view.getStatesJTree().getPathForRow(i);
				        if (view.getStatesJTree().isExpanded(i))
				        {
				        	expansions += path.toString();
				        	expansions += ",";
				        }
				    }
					// Save currently selected row
					int selectionRow = view.getStatesJTree().getLeadSelectionRow();
					// Display updated states and stations
					showTree(view.getStatesJTree(), updateStates);
					// Restore expanded nodes
					for (int i = 0; i < view.getStatesJTree().getRowCount(); ++i)
					{
				        TreePath path = view.getStatesJTree().getPathForRow(i);
				        if (expansions.contains(path.toString()))
				        	view.getStatesJTree().expandRow(i);  
				    }
					// Restore currently selected row
					view.getStatesJTree().setSelectionRow(selectionRow);
					view.getBtnRefresh().setText(originalText);
					// Update current station table
					if (currentStation != null)
						updateTable(currentStation, currentMonth);
				}
			}
		}).start();
	}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e)
	{
		String[] prefs = new String[2];
		prefs[0] = "posX: " + view.getFrame().getLocation().x;
		prefs[1] = "posY: " + view.getFrame().getLocation().y;
		backup.writeWindowPrefs(prefs);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e)
	{
		String[] prefs = backup.getWindowPrefs();
		if (prefs != null)
		{
			int x = Integer.parseInt(prefs[0]);
			int y = Integer.parseInt(prefs[1]);
			// Make sure window isn't off-screen
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int frameHeight = view.getFrame().getHeight();
			int frameWidth = view.getFrame().getWidth();
			if (x < 0)
				x = 0;
			else if (x + frameWidth > dim.width)
				x = dim.width - frameWidth;
			if (y < 0)
				y = 0;
			else if (y + frameHeight > dim.height)
				y = dim.height - frameHeight;
			view.getFrame().setLocation(x, y);
		}
	}
}
