package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import scraper.Scraper;
//TODO Class should quite possibly have static methods.
public class Control implements Runnable {
	public static int favouriteNum;
	private File stationStorage = new File("StationData.txt");
	private PrintWriter writer;
	private Station[] favouriteStations= new Station[favouriteNum];
	private BufferedReader reader;
	
	
	public Control() throws IOException {
		writer= new PrintWriter(new BufferedWriter(new FileWriter(stationStorage))); //Throws IOException, perhaps should be handled here rather than propagating out the method
		BufferedReader reader=new BufferedReader(new FileReader(stationStorage));
	}
	public void run()
	{
		loadFromFile();
		//Stuff and things
		printToFile();
	}
	public static void printToFile()
	{
		
	}
	
	public static void loadFromFile()
	{
		
	}

	public boolean addToFavourites(Station newStation){ //Add a station to the list of favourite stations
		for (int i=0;i<favouriteNum;i++)
		{
			//Loop through the list of stations and add the new station to the most recent station
			if (favouriteStations[i]==null)
			{
				favouriteStations[i]=newStation; 
				return true;
			}
		}
		//The station list is full
		return false;
	}
	
	public void removeFromFavourites(int n){// Remove the 
		favouriteStations[n]=null;
	}
	public Station[] getFavouriteStations() {
		return favouriteStations;
	}
	public void setFavouriteStations(Station[] favouriteStations) {
		this.favouriteStations = favouriteStations;
	};

}
