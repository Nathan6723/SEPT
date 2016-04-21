package data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeSet;

import org.jsoup.nodes.Element;

public class Data
{
	private final static String BOMURL = "http://www.bom.gov.au";
	private final static String DWOURL = "http://www.bom.gov.au/climate/dwo/";
	private final static String[] LATESTURL = {"http://www.bom.gov.au/", "/observations/", "all.shtml"};
	private final static String[] STATES = {"ant", "nsw", "nt", "qld", "sa", "tas", "vic", "wa"};
	private final static String[] STATENAMES = {"Australia's Antarctic Bases", "New South Wales","Northern Territory",
			"Queensland", "South Australia", "Tasmania", "Victoria", "Western Australia"};

	private Backup backup = new Backup();
	private Scraper scraper = new Scraper();
	
	private boolean gettingStates;
	private TreeSet<State> states = new TreeSet<>();
	private TreeSet<State> favourites = new TreeSet<>();
	
	public TreeSet<State> getFavourites()
	{
		return favourites;
	}
	
	public boolean isGettingStates()
	{
		return gettingStates;
	}
	
	public TreeSet<State> getFavouritesBackup()
	{
		TreeSet<State> favouritesBackup = backup.getFavouritesBackup();
		if (favouritesBackup == null)
			return null;
		else
			return favourites = favouritesBackup;
	}
	
	public void backupFavourites()
	{
		backup.writeFavouritesBackup(favourites);
	}
	
	public TreeSet<State> getStates()
	{
		return states;
	}
	
	public TreeSet<State> getStatesUpdate()
	{
		gettingStates = true;
		getDWOStationNames();
		//getLatestStationNames();
		backup.writeStatesBackup(states);
		gettingStates = false;
		return states;
	}
	
	public TreeSet<State> getStatesBackup()
	{
		TreeSet<State> statesBackup = backup.getStatesBackup();
		if (statesBackup == null)
			return null;
		else
			return states = statesBackup;
	}

	private void getDWOStationNames()
	{
		ArrayList<Element> stateLinks = scraper.getLinksFromTag(DWOURL, "th");
		for (Element stateLink : stateLinks)
		{
			State state = new State(stateLink.text());
			String link = DWOURL + stateLink.attr("href");
			state.setDWOURL(link);
			ArrayList<Element> stationLinks = scraper.getLinksFromTag(link, "th");
			if (stationLinks.size() == 0)
			{
				ArrayList<Element> categories = scraper.getLinksFromTag(link, "h2");
				for (Element category : categories)
					stationLinks.addAll(scraper.getLinksFromTag(BOMURL + category.attr("href"), "th"));
			}
			for (Element stationLink : stationLinks)
			{
				Station station = new Station(stationLink.text());
				String id = stationLink.attr("href");
				id = id.substring(id.indexOf("IDCJDW"), id.indexOf("latest") - 1);
				Calendar calendar = Calendar.getInstance();
				for (int i = 0; i < 6; ++i)
				{
					int month = calendar.get(Calendar.MONTH) + 1;
					int year = calendar.get(Calendar.YEAR);
					String date = "" + year + (month < 10 ? "0" : "") + month;
					link = DWOURL + date + "/html/" + id + "." + date + ".shtml";
					station.addDWOURL(link);
				}
				state.addStation(station);
			}
			states.add(state);
		}
	}
	
	@SuppressWarnings("unused")
	private void getLatestStationNames()
	{
		for (int i = 0; i < STATES.length; ++i)
		{
			String stateURL = LATESTURL[0] + STATES[i] + LATESTURL[1] + STATES[i] + LATESTURL[2];
			State state = null;
			Iterator<State> iterState = states.iterator();
			while (iterState.hasNext())
			{
				state = iterState.next();
				if (state.getName().equals(STATENAMES[i]))
					break;
			}
			state.setLatestURL(stateURL);
			ArrayList<Element> stationLinks = scraper.getLinksFromTag(stateURL, "th");
			for (Element stationLink : stationLinks)
			{
				String text = stationLink.text();
				text = text.endsWith("*") ? text.substring(0, text.length()-1) : text;
				Station station = new Station(text);
				Iterator<Station> iterStation = state.getStations().iterator();
				boolean found = false;
				while (iterStation.hasNext())
				{
					station = iterStation.next();
					if (station.getName().equals(text))
					{
						found = true;
						break;
					}
				}
				station.setLatestURL(BOMURL + stationLink.attr("href"));
				if (!found)
					state.addStation(station);
			}
			states.add(state);
		}
	}
	
	public Station getStation(TreeSet<State> searchStates, String stateName, String stationName)
	{
		Iterator<State> iterState = searchStates.iterator();
		while (iterState.hasNext())
		{
			State state = iterState.next();
			if (state.getName().equals(stateName))
			{
				Iterator<Station> iterStation = state.getStations().iterator();
				while (iterStation.hasNext())
				{
					Station station = iterStation.next();
					if (station.getName().equals(stationName))
						return station;
				}
			}
		}
		return null;
	}
	
	public ArrayList<ArrayList<String>> getDWOStationData(Station station, int month)
	{
		return scraper.getTableData(station.getDWOURLs().get(month), 0);
	}
	
	public ArrayList<ArrayList<String>> getLatestStationData(Station station, int day)
	{
		return scraper.getTableData(station.getLatestURL(), day+1);
	}
}
