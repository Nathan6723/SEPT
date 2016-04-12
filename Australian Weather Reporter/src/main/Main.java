package main;

import scraper.Scraper;

public class Main
{
	public static void main(String[] args)
	{
		Scraper scraper = new Scraper();
		scraper.findURLsInPage("http://www.bom.gov.au/climate/dwo/");
	}



}