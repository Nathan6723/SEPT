package scraper;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Scraper
{
	public String findURLsInPage(String url)
	{
		Document doc;
		try
		{
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		System.out.print(doc.toString());
		return doc.toString();
	}
}
