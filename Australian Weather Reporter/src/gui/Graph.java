package gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

@SuppressWarnings("serial")
public class Graph extends JFrame
{
	public Graph(String stationName, float[] values) throws HeadlessException
	{
		//Based on tutorials from http://www.tutorialspoint.com/jfreechart/jfreechart_line_chart.htm
		super("Monthly Temperature Statistics");        
		JFreeChart lineChart = ChartFactory.createLineChart
	    (
	    	stationName + " Temperature Graph",           
			"Measurement",            
			"Temperature",            
			createDataset(values), //Probably have to pass data into this constructor, but I'm not sure what          
			PlotOrientation.VERTICAL,           
			true, true, false
		);
		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new Dimension(500, 400));
		setContentPane(chartPanel);
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
		setVisible(true);
	}

	private CategoryDataset createDataset(float[] values)
	{
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		 // Maxes
		 if (values[0] != -Float.MAX_VALUE)
			 dataset.addValue(values[0], "Maximum Temp", "Min");
		 if (values[1] != -Float.MAX_VALUE)
			 dataset.addValue(values[1], "Maximum Temp", "Max");
		 if (values[2] != -Float.MAX_VALUE)
			 dataset.addValue(values[2], "Maximum Temp", "9 AM");
		 if (values[3] != -Float.MAX_VALUE)
			 dataset.addValue(values[3], "Maximum Temp", "3 PM");
		 // Averages
		 if (values[4] != 0)
			 dataset.addValue(values[4], "Average Temp", "Min");
		 if (values[5] != 0)
			 dataset.addValue(values[5], "Average Temp", "Max");
		 if (values[6] != 0)
			 dataset.addValue(values[6], "Average Temp", "9 AM");
		 if (values[7] != 0)
			 dataset.addValue(values[7], "Average Temp", "3 PM");
		 // Minimums
		 if (values[8] != Float.MAX_VALUE)
			 dataset.addValue(values[8], "Minimum Temp", "Min");
		 if (values[9] != Float.MAX_VALUE)
			 dataset.addValue(values[9], "Minimum Temp", "Max");
		 if (values[10] != Float.MAX_VALUE)
			 dataset.addValue(values[10], "Minimum Temp", "9 AM");
		 if (values[11] != Float.MAX_VALUE)
			 dataset.addValue(values[11], "Minimum Temp", "3 PM");
		 return dataset;
	}

	public Graph(String s, GraphicsConfiguration g)
	{
		super(s, g);
	}
}
