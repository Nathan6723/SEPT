package main;

import java.util.Iterator;
import java.util.TreeSet;

import data.Backup;
import data.Data;
import data.State;
import data.Station;

//TODO Class should quite possibly have static methods.
public class Control implements Runnable{
	private TreeSet<Station> favouriteStations;
	private Data data;
	
	
	public Control() {
		favouriteStations=Backup.getJSONFavourites();
	}
	public static void main(String[] args)
	{
		Control program = new Control();
		Thread backgroundProgram = new Thread(program);
		backgroundProgram.run();
		Backup.writeJSONFavourites(program.getFavouriteStations());
		
	}
	public void refresh()
	{
		data = Data.GetInstance();
		//TODO Update favourite station data from data
	}

	public TreeSet<Station> getStations(String stateName)
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
	public TreeSet<Station> getStations(State state)
	{//returns a station list of an associated given state TODO probably unnecessary
		TreeSet<Station> stationList=null;
		stationList=state.getStations();
		return stationList;
	}

	public void addToFavourites(Station newStation) //Add a station to the list of favourite stations
	{
		favouriteStations.add(newStation);
	}
	
	public void removeFromFavourites(Station remStation){// Remove the given station from the list of stations
		favouriteStations.remove(remStation);
	}
	public TreeSet<Station> getFavouriteStations() {
		return favouriteStations;
	}
	public void setFavouriteStations(TreeSet<Station> favouriteStations) {
		this.favouriteStations = favouriteStations;
	}
	@Override
	public void run() {//Thread automatically the data every half hour
		try 
		{
			while(true)
			{
				this.refresh();
				Thread.sleep(1800000);
			}
		} catch (InterruptedException e) 
		{
		e.printStackTrace();
		}		
	}

}
