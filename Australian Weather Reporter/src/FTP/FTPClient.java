package FTP;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class FTPClient
{	
	private Scanner fileStream;
	private Scanner inputStream;
	private PrintStream outputStream;
	private boolean disconnected = false;
	public final static String BOMIP = "ftp.bom.gov.au";
	public final static int FTPPORT = 21;
	// State as key and list of stations as entries
	Map<String, ArrayList<String>> stateStations;
	public boolean ConnectFTP(String ip, int port)
	{
		try
		{
			@SuppressWarnings("resource")
			// Connect to weather site
			Socket ftpConnection = new Socket(ip, port);
			// Setup streams
			outputStream = new PrintStream(ftpConnection.getOutputStream());	
			inputStream = new Scanner(ftpConnection.getInputStream());
			inputStream.useDelimiter("\r\n");
			// Login as guest
			outputStream.println("USER anonymous");
			outputStream.println("PASS guest");
			outputStream.println("CWD anon/gen/clim_data/IDCKWCDEA0/tables");
			// Clear non-relevant input
			while (!inputStream.nextLine().contains("250"));
			// Request passive mode to avoid firewall issues
			outputStream.println("PASV");
			String connectionInfo = inputStream.nextLine();
			// Extract IP and port info from message
			connectionInfo = connectionInfo.substring(connectionInfo.indexOf("(") + 1, connectionInfo.indexOf(")"));
			StringTokenizer tokenizer = new StringTokenizer(connectionInfo, ",");
			// Construct IP address
			String fIP = tokenizer.nextToken() + "." + tokenizer.nextToken()
					+ "." + tokenizer.nextToken() + "." + tokenizer.nextToken();
			int fPort = Integer.parseInt(tokenizer.nextToken()) * 256 + Integer.parseInt(tokenizer.nextToken());
			// Connect to file transfer port
			@SuppressWarnings("resource")
			Socket fileSocket = new Socket(fIP, fPort);
			fileStream = new Scanner(fileSocket.getInputStream());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		RetriveStations();
		return true;
	}
	public void RetriveStations()
	{
		stateStations = new LinkedHashMap<>();
		ArrayList<String> stations;
		outputStream.println("RETR stations_db.txt");
		while (fileStream.hasNextLine())
		{
			String line = fileStream.nextLine();
			StringTokenizer tokenizer = new StringTokenizer(line, " ");
			tokenizer.nextToken();
			String state = tokenizer.nextToken();
			if (!stateStations.containsKey(state))
				stateStations.put(state, new ArrayList<String>());
			stations = stateStations.get(state);
			tokenizer.nextToken();
			String name = "";
			for (int i = 0; i < tokenizer.countTokens() - 3; i++)
				name += tokenizer.nextToken() + " ";
			stations.add(name);
			for (int i = 0; i < 3; i++)
				tokenizer.nextToken();
		}
		// Sort map by state name
		
	}
	public ArrayList<String> GetStates()
	{
		ArrayList<String> states = new ArrayList<>();
		Iterator<String> iterator = stateStations.keySet().iterator();
		while (iterator.hasNext())
			states.add(iterator.next());
		return states;
	}
	public ArrayList<String> GetStations(String state)
	{
		return stateStations.get(state);
	}
	public void GetStationData(String state, String station)
	{
		outputStream.println("CWD " + state + "/" + station);
		inputStream.nextLine();
		station.replace(" ", "_");
		Calendar calendar = Calendar.getInstance();
		int month = calendar.get(Calendar.MONTH) + 1;
		outputStream.println("RETR " + station + "-" + calendar.get(Calendar.YEAR) 
				+ (month < 10 ? 0 : "") + month +".csv");
		inputStream.nextLine();
		outputStream.println("PWD");
		System.out.println(inputStream.nextLine());
	}
	public ArrayList<String> GetUpdates()
	{
		ArrayList<String> data = new ArrayList<>();
		while (inputStream.hasNext())
		{
			String token = inputStream.next();
			System.out.println(token);
			if (token.startsWith("221"))
			{
				disconnected = true;
				break;
			}
			data.add(token);
		}
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return data;
	}
	public boolean IsConnected()
	{
		if (disconnected)
			return false;
		return true;
	}
	public void QuitFTP()
	{
		outputStream.println("QUIT");
	}
}