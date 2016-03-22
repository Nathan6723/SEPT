package Main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import FTP.FTPClient;

public class Main
{
	public static void main(String[] args)
	{
		FTPClient ftp = new FTPClient();
		if (ftp.ConnectFTP(FTPClient.BOMIP, FTPClient.FTPPORT))
			System.out.println("Connected successfully");
		else
			System.out.println("Failed to connect");
		ftp.GetStations("vic");
		ftp.QuitFTP();
	}
}