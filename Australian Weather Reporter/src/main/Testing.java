package main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import data.Data;
import data.State;
import data.Station;

public class Testing
{
	public static void main(String[] args)
	{
		long startTime = System.nanoTime();
		
		Data data = Data.GetInstance();
		
		//TreeSet<State> states = data.getStatesBackup();
		
		//if (states == null)
		TreeSet<State> states = data.getStates();
		
		//printStates(states);
		
		System.out.println();

		printTable(data.getDWOStationData(states.first().getStations().first(), 0));

		double timeDiff = ((double)(System.nanoTime() - startTime) / Math.pow(10, 9));
		String time = String.format("%f", timeDiff);
		System.out.println("\n" + time + " seconds");
	}

	public static void printStates(TreeSet<State> states)
	{
		boolean first = true;
		Iterator<State> iterState = states.iterator();
		while (iterState.hasNext())
		{
			State state = iterState.next();
			System.out.println((first ? "" : "\n") + state.getName() + ":\n");
			Iterator<Station> iterStation = state.getStations().iterator();
			while (iterStation.hasNext())
				System.out.println(iterStation.next().getName());
			first = false;
		}
	}
	
	public static void printTable(ArrayList<ArrayList<String>> rows)
	{
		Iterator<ArrayList<String>> iterAr = rows.iterator();
		while (iterAr.hasNext())
		{
			ArrayList<String> row = iterAr.next();
			Iterator<String> iterS = row.iterator();
			while (iterS.hasNext())
				System.out.print(iterS.next() + " ");
			System.out.println();
		}
	}
}
