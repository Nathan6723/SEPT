package main;

import gui.Frame1;

import java.util.Iterator;
import java.util.TreeSet;

import data.Backup;
import data.Data;
import data.State;
import data.Station;

//TODO Class should quite possibly have static methods.
public class Control implements Runnable{
	static private TreeSet<Station> favouriteStations;
	static private Data data;
	
	
	public Control() {
		//favouriteStations=Backup.getJSONFavourites(); //TODO BROKEN
	}
	public static void main(String[] args)
	{
		/* TODO Control program = new Control();
		Thread backgroundProgram = new Thread(program);
		backgroundProgram.run();*/
		boolean quit=false;
		while(quit==false)
		{
			Frame1 frame = new Frame1();
			frame.mainFrame();
			//quit=drawGUI();
			quit=true;
		}
		//TODO Backup.writeJSONFavourites(program.getFavouriteStations());
		
	}
	public void refresh()
	{
		//TODO update data instance?
		data = Data.GetInstance();
		// Updates favouriteStations from data
		// Implemented by iterating through all three tree sets. Very inefficient could be better? -Michael
		Iterator<State> stateIterator = data.getStates().iterator();
		Iterator<Station> stationIterator;
		Iterator<Station> favIterator = favouriteStations.iterator();
		while (stateIterator.hasNext())
		{
			State currentState = stateIterator.next();
			stationIterator = currentState.getStations().iterator();
			while (stationIterator.hasNext())
			{
				Station currentStation=stationIterator.next();
				while(favIterator.hasNext())
				{
					Station currentFav = favIterator.next();
					if (currentStation.getName().equals(currentFav.getName()))
					{
						removeFromFavourites(currentFav);
						addToFavourites(currentStation);
					}
				}
			}
		}
	}
	
	static public void drawGraph(Station graphStation)
	{
		//TODO, needs graph class to exist
	}

	static public TreeSet<Station> getStations(String stateName)
	{//Takes a String State name and returns the stations associated with that State	
		TreeSet<Station> stationList=null;
		TreeSet<State> stateList=data.getStates();
		Iterator<State> iterator=stateList.iterator();
		while (iterator.hasNext())
		{
			State currentState =iterator.next();
			if (currentState.getName().equals(stateName))
			{
				stationList=currentState.getStations();
			}
		}
		return stationList;
	}
	static public TreeSet<Station> getStations(State state)
	{//returns a station list of an associated given state TODO probably unnecessary
		TreeSet<Station> stationList=null;
		stationList=state.getStations();
		return stationList;
	}

	static public void addToFavourites(Station newStation) //Add a station to the list of favourite stations
	{
		favouriteStations.add(newStation);
	}
	
	static public void removeFromFavourites(Station remStation){// Remove the given station from the list of stations
		favouriteStations.remove(remStation);
	}
	static public TreeSet<Station> getFavouriteStations() {
		return favouriteStations;
	}
	static public void setFavouriteStations(TreeSet<Station> favouriteStations) {
		Control.favouriteStations = favouriteStations;
	}
	@Override
	public void run() {//Thread automatically refreshes the data every half hour
		try 
		{
			while(true)
			{
				//this.refresh(); //CAUSES CRASH
				Thread.sleep(1800000);
			}
		} catch (InterruptedException e) 
		{
		e.printStackTrace();
		}		
	}

}
