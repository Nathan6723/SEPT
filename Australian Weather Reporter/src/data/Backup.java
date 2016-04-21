package data;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Backup
{
	private final static String STATES_FILENAME = "States.json";
	private final static String FAVOURITES_FILENAME = "Favourites.json";
	
	public void writeStatesBackup(TreeSet<State> states)
	{
		writeJSONObject(states, STATES_FILENAME);
	}
	
	public void writeFavouritesBackup(TreeSet<State> states)
	{
		writeJSONObject(states, FAVOURITES_FILENAME);
	}
	
	private void writeJSONObject(Object object, String filename)
	{
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try
		{
			String json = ow.writeValueAsString(object);
			PrintWriter pw = new PrintWriter(filename);
			pw.print(json);
			pw.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public TreeSet<State> getStatesBackup()
	{
		return getStatesBackup(STATES_FILENAME);
	}
	
	public TreeSet<State> getFavouritesBackup()
	{
		return getStatesBackup(FAVOURITES_FILENAME);
	}
	
	private <T> T getStatesBackup(String filename)
	{
		try
		{
			Path path = Paths.get(filename);
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
}
