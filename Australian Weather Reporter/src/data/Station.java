package data;

import java.util.ArrayList;
import java.util.TreeSet;

public class Station implements Comparable<Station>
{
	private String name;
	private String latestURL;
	
	private ArrayList<String> dwoURLs = new ArrayList<>();
	private TreeSet<Table> dwoTables = new TreeSet<Table>();
	private TreeSet<Table> latestTables = new TreeSet<Table>();
	
	public Station () {}
	
	public Station(String name)
	{
		this.name = name;
	}
	
	public TreeSet<Table> getDWOTables()
	{
		return dwoTables;
	}
	
	public TreeSet<Table> getLatestTables()
	{
		return latestTables;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<String> getDWOURLs()
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
