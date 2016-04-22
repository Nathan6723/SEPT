package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Backup
{
	private final static String STATES_FILENAME = "States.json";
	private final static String FAVOURITES_FILENAME = "Favourites.json";
	private final static String PREFERENCES_FILENAME = "Preferences.ini";
	
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
	
	private TreeSet<State> getStatesBackup(String filename)
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
			return null;
		}
	}
	
	public void writeWindowPrefs(String[] prefs)
	{
		try
		{
			PrintWriter writer = new PrintWriter(PREFERENCES_FILENAME);
			for (int i = 0; i < prefs.length; ++i)
				writer.println(prefs[i]);
			writer.flush();
			writer.close();
		}
		catch (FileNotFoundException e) {}	
	}
	
	public String[] getWindowPrefs()
	{
		Path path = Paths.get(PREFERENCES_FILENAME);
		if (!Files.exists(path))
			return null;
		String[] prefs = new String[2];
		try
		{
			Scanner scan = new Scanner(new File(PREFERENCES_FILENAME));
			prefs[0] = scan.nextLine();
			prefs[0] = prefs[0].substring(prefs[0].indexOf(':') + 2, prefs[0].length());
			prefs[1] = scan.nextLine();
			prefs[1] = prefs[1].substring(prefs[1].indexOf(':') + 2, prefs[1].length());
			scan.close();
		}
		catch (Exception e)
		{
			return null;
		}
		return prefs;
	}
}
