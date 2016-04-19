package data;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper
{
	public Document Connect(String url)
	{
		try
		{
			return Jsoup.connect(url).maxBodySize(0).timeout(0).get();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Element> getLinksFromTag(String url, String tag)
	{
		Document doc = Connect(url);
		Elements elements = doc.getElementsByTag(tag);
		ArrayList<Element> urls = new ArrayList<>();
		for (Element element : elements)
		{
			Elements links = element.getElementsByTag("a");
			for (Element link : links)
				urls.add(link);
		}
		return urls;
	}
	
	public ArrayList<ArrayList<String>> getTableData(String url, int tableNum)
	{
		Document doc = Connect(url);
		Elements rows = doc.getElementsByTag("tbody").get(tableNum).getElementsByTag("tr");
		ArrayList<ArrayList<String>> dataAr = new ArrayList<ArrayList<String>>();
		for (Element row : rows)
		{
			ArrayList<String> rowAr = new ArrayList<>();
			Elements datas = row.getElementsByTag("td");
			for (Element data : datas)
			{
				if (data.text().equals(String.valueOf('\u00A0')) && datas.size() > 1)
					rowAr.add("-");
				else
					rowAr.add(data.text());
			}
			dataAr.add(rowAr);
		}
		return dataAr;
	}
}
