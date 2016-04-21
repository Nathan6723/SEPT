package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import data.Data;
import data.State;
import data.Station;
import gui.View;

public class Controller implements ActionListener, TreeSelectionListener
{
	private Data data = new Data();
	private View view = new View();
	
	private Station currentStation;
	private TreePath lastPath;
	
	private final static Object[] COLUMN_NAMES = new Object[]{"Day", "Min", "Max", "Rain(mm)",
			"Evap(mm)", "Sun(hours)", "Dir", "Spd", "Time", "Temp", "RH", "Cld", "Dir", "Spd",
			"MSLP", "Temp", "RH", "Cld", "Dir", "Spd", "MSLP"};
	
	public Controller()
	{
		addListeners();
		showBackupStates();
		showBackupFavourites();
		startRefresh();
	}
	
	private void addListeners()
	{
		view.getBtnRefresh().addActionListener(this);
		view.getBtnAddToFavourites().addActionListener(this);
		view.getBtnRemoveFromFavourites().addActionListener(this);
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
			Station station = currentStation = data.getStation(data.getStates(), stateName, stationName);
			ArrayList<ArrayList<String>> table = data.getDWOStationData(station, 0);
			// Somtimes data isn't available
			// Need to display a message
			if (table == null)
			{
				view.getJTable().setModel(new DefaultTableModel());
				return;
			}
			DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
			for (int i = 0; i < table.size(); ++i)
				model.addRow(table.get(i).toArray());
			view.getJTable().setModel(model);
		}
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
		data.backupFavourites();
		showTree(view.getFavouritesJTree(), data.getFavourites());
		view.getBtnAddToFavourites().setVisible(false);
		view.getBtnRemoveFromFavourites().setVisible(true);
	}
	
	private void removeStationFromFavourites()
	{
		if (lastPath == null)
			return;
		String stateName = lastPath.getPathComponent(1).toString();
		String stationName = lastPath.getPathComponent(2).toString();
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
					}
				}
			}
		}
		showTree(view.getFavouritesJTree(), data.getFavourites());
		view.getBtnRemoveFromFavourites().setVisible(false);
		view.getBtnAddToFavourites().setVisible(true);
	}
	
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
					showTree(view.getStatesJTree(), data.getStatesUpdate());
			}
		}).start();
	}
}
