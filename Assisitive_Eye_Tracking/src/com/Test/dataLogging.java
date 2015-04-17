package com.Test;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class dataLogging {

	private Path filePath;
	private ArrayList<Point> positionList;
	private int numObjs;
	/**
	 * @param args
	 */
	public dataLogging(Path path)
	{
		/** Class to store Java Point objects and eventually write
		 *  them to file. Accepts a path to the output file.*/
		this.filePath = path;
		this.positionList = new ArrayList<Point>();
		this.numObjs = 0;
	}
	
	public int addPoint(Point p)
	{
		/** Add a point to the list, return the number of
		 * items currently in the list.
		 */
		positionList.add(p);
		numObjs += 1;
		System.out.println(numObjs);
		return numObjs;
	}
	
	public boolean writeFile()
	{
		/** Write the data collected to a file. */
		// Select the ASCII character set because we don't need Unicode
		Charset ascii  = Charset.forName("US-ASCII");
		try
		{
			// Create the buffered writer obj
			BufferedWriter writer = Files.newBufferedWriter(this.filePath, ascii);
			// Write the header, going for Tab-separated values
			writer.write("Index:,X-position:,Y-position\n");
			// Write the data
			for (int i = 0; i < numObjs; i++)
			{
				Point p = positionList.get(i);
				System.out.println(p);
				writer.write(Integer.toString(i) + "," + Integer.toString(p.x) + "," + Integer.toString(p.y) + "\n");
			}
			// Clear the positionList
			positionList.clear();
			numObjs = 0;
			// Close the buffered writer
			writer.close();
			return true;
		}
		catch (IOException e)
		{
			System.out.println(e);
			return false;
		}
	}
	
	public Path getFilePath()
	{
		return this.filePath;
	}
	
	public int getLengthOfData()
	{
		return this.numObjs;
	}
}
