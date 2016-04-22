package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import data.Station;
import data.Table;

public class DrawGraphWindow extends JFrame {

	public DrawGraphWindow(String applicationTitle) throws HeadlessException {
		//Based on tutorials from http://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm
		super( applicationTitle );        
	      JFreeChart lineChart = ChartFactory.createLineChart(
	         "Daily Weather Data " + "StationName",           
	         "Date",            
	         "Temperature",            
	         createDataset(), //Probably have to pass data into this constructor, but I'm not sure what          
	         PlotOrientation.VERTICAL,           
	         true, true, false);
	      ChartPanel chartPanel = new ChartPanel( lineChart );
	      chartPanel.setPreferredSize(new Dimension(500,500));
	      setContentPane( chartPanel );
	}

	private CategoryDataset createDataset() {
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		 //TreeSet<Table> dataTable = station.getDWOTables();
		 dataset.addValue(28, "Maximum Temp", "March 15");
		 dataset.addValue(25, "Maximum Temp", "March 16");
		 dataset.addValue(18, "Minimum Temp", "March 15");
		 dataset.addValue(17, "Minimum Temp", "March 16");
		 //dataset.addValue(value, rowKey, columnKey);
		 return dataset;
	}

	public DrawGraphWindow(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
