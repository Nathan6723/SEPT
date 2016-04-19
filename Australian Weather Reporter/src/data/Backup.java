package data;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Backup
{
	private final static String STATESFILENAME = "states.json";
	private final static String FAVOURITESFILENAME = "FavStations.json";
	
	public void writeJSONObject(TreeSet<State> states)
	{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try
		{
			String json = ow.writeValueAsString(states);
			PrintWriter pw = new PrintWriter(STATESFILENAME);
			pw.print(json);
			pw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void writeJSONFavourites(TreeSet<Station> favouriteStations)
	{// This is based upon the above method. I'm not familiar with JSON objects, so I assume it works. TODO needs testing -Michael
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try
		{
			String json = ow.writeValueAsString(favouriteStations);
			PrintWriter pw = new PrintWriter(FAVOURITESFILENAME);
			pw.print(json);
			pw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public TreeSet<State> getJSONObject()
	{
		try
		{
			Path path = Paths.get(STATESFILENAME);
			if (!Files.exists(path))
				return null;
			String json = new String(Files.readAllBytes(path));
			if (json.isEmpty())
				return null;
			ObjectMapper mapper = new ObjectMapper();
			JavaType type = mapper.getTypeFactory().constructCollectionType(TreeSet.class, State.class);
			return mapper.readValue(json, type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public static TreeSet<Station> getJSONFavourites()
	{//// This is based upon the above method. I'm not familiar with JSON objects, so I assume it works. TODO needs testing -Michael
		try
		{
			Path path = Paths.get(FAVOURITESFILENAME);
			if (!Files.exists(path))
				return null;
			String json = new String(Files.readAllBytes(path));
			if (json.isEmpty())
				return null;
			ObjectMapper mapper = new ObjectMapper();
			JavaType type = mapper.getTypeFactory().constructCollectionType(TreeSet.class, Station.class);
			return mapper.readValue(json, type);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
