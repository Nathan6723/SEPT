package data;

import java.util.HashSet;
import java.util.TreeSet;

public class Station implements Comparable<Station>
{
	private String name;
	private HashSet<String> dwoURLs;
	private String latestURL;
	
	public TreeSet<Table> DWOTables;
	public TreeSet<Table> LatestTables;
	
	public Station () {}
	
	public Station(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public HashSet<String> getDWOURLs()
	{
		return dwoURLs;
	}
	
	public void addDWOURL(String dwoURL)
	{
		dwoURLs.add(dwoURL);
	}

	public String getLatestURL()
	{
		return latestURL;
	}

	public void setLatestURL(String latestURL)
	{
		this.latestURL = latestURL;
	}
	
	@Override
	public int compareTo(Station station)
	{
		return this.getName().compareTo(station.getName());
	}
}
