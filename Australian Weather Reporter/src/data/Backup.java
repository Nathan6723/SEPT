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
	private final static String STATESFILENAME = "states.json";
	
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
}
