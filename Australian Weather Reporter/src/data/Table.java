package data;

import java.util.ArrayList;

public class Table implements Comparable<Table>
{
	private int index;
	private ArrayList<ArrayList<String>> rows = new ArrayList<>();
	
	public Table() {}
	
	public Table(ArrayList<ArrayList<String>> rows)
	{
		this.rows = rows;
	}
	
	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public ArrayList<ArrayList<String>> getRows()
	{
		return rows;
	}
	
	@Override
	public int compareTo(Table table)
	{
		return index - table.getIndex();
	}
}
