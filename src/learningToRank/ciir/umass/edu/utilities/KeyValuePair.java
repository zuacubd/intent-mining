/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package learningToRank.ciir.umass.edu.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vdang
 */
public class KeyValuePair {
	protected List<String> keys = new ArrayList<String>();;
	protected List<String> values = new ArrayList<String>();;
	
	public KeyValuePair(String text)
	{
		try {
			int idx = text.lastIndexOf("#");
			if(idx != -1)//remove description at the end of the line (if any)
				text = text.substring(0, idx).trim();//remove the comment part at the end of the line

			String[] fs = text.split(" ");
			for(int i=0;i<fs.length;i++)
			{
				fs[i] = fs[i].trim();
				if(fs[i].compareTo("")==0)
					continue;
				keys.add(getKey(fs[i]));
				values.add(getValue(fs[i]));
				
			}
		}
		catch(Exception ex)
		{
			System.out.println("Error in KeyValuePair(text) constructor");
		}
	}
	public List<String> keys()
	{
		return keys;
	}
	public List<String> values()
	{
		return values;
	}
	
	private String getKey(String pair)
	{
		return pair.substring(0, pair.indexOf(":"));
	}
	private String getValue(String pair)
	{
		return pair.substring(pair.lastIndexOf(":")+1);
	}
}
