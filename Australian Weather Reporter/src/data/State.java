package data;

import java.util.TreeSet;

public class State implements Comparable<State>
{
	private String name;
	private String dwoURL;
	private String latestURL;

	private TreeSet<Station> stations = new TreeSet<>();
	
	public State() {}
	
	public State(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addStation(Station station)
	{
		stations.add(station);
	}
	
	public TreeSet<Station> getStations()
	{
		return stations;
	}
	
	public String getDWOURL()
	{
		return dwoURL;
	}

	public void setDWOURL(String dwoURL)
	{
		this.dwoURL = dwoURL;
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
	public int compareTo(State state)
	{
		return getName().compareTo(state.getName());
	}
}
