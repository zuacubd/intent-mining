/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package learningToRank.ciir.umass.edu.learning;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import learningToRank.ciir.umass.edu.learning.RankList;
import learningToRank.ciir.umass.edu.metric.MetricScorer;
import learningToRank.ciir.umass.edu.utilities.FileUtils;
import learningToRank.ciir.umass.edu.utilities.MergeSorter;

/**
 * @author vdang
 * 
 * This class implements the generic Ranker interface. Each ranking algorithm implemented has to extend this class. 
 */
public class Ranker {
	public static boolean verbose = true;

	protected List<RankList> samples = new ArrayList<RankList>();//training samples
	protected int[] features = null;
	protected MetricScorer scorer = null;
	protected double scoreOnTrainingData = 0.0;
	protected double bestScoreOnValidationData = 0.0;
	
	protected List<RankList> validationSamples = null;
	
	protected Ranker()
	{
		
	}	
	protected Ranker(List<RankList> samples, int[] features, MetricScorer scorer)
	{
		this.samples = samples;
		this.features = features;
		this.scorer = scorer;
	}
	
	//Utility functions
	public void setTrainingSet(List<RankList> samples)
	{
		this.samples = samples;
	
	}
	public void setFeatures(int[] features)
	{
		this.features = features;	
	}
	public void setValidationSet(List<RankList> samples)
	{
		this.validationSamples = samples;
	}
	public void setMetricScorer(MetricScorer scorer)
	{
		this.scorer = scorer;
	}
	
	public double getScoreOnTrainingData()
	{
		return scoreOnTrainingData;
	}
	public double getScoreOnValidationData()
	{
		return bestScoreOnValidationData;
	}
	public int[] getFeatures()
	{
		return features;
	}
	
	public RankList rank(RankList rl)
	{
		double[] scores = new double[rl.size()];
		for(int i=0;i<rl.size();i++)
			scores[i] = eval(rl.get(i));
		int[] idx = MergeSorter.sort(scores, false);
		return new RankList(rl, idx);
	}
	public List<RankList> rank(List<RankList> l)
	{
		List<RankList> ll = new ArrayList<RankList>();
		for(int i=0;i<l.size();i++)
			ll.add(rank(l.get(i)));
		return ll;
	}
	public void save(String modelFile) 
	{
		FileUtils.write(modelFile, "ASCII", model());
	}
	
	protected void PRINT(String msg)
	{
		if(verbose)
			System.out.print(msg);
	}
	protected void PRINTLN(String msg)
	{
		if(verbose)
			System.out.println(msg);
	}
	protected void PRINT(int[] len, String[] msgs)
	{
		if(verbose)
		{
			for(int i=0;i<msgs.length;i++)
			{
				String msg = msgs[i];
				if(msg.length() > len[i])
					msg = msg.substring(0, len[i]);
				else
					while(msg.length() < len[i])
						msg += " ";
				System.out.print(msg + " | ");
			}
		}
	}
	protected void PRINTLN(int[] len, String[] msgs)
	{
		PRINT(len, msgs);
		PRINTLN("");
	}
	protected void PRINTTIME()
	{
		DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
	}
	protected void PRINT_MEMORY_USAGE()
	{
		System.out.println("***** " + Runtime.getRuntime().freeMemory() + " / " + Runtime.getRuntime().maxMemory());
	}
	
	protected void copy(double[] source, double[] target)
	{
		for(int j=0;j<source.length;j++)
			target[j] = source[j];
	}
	
	/**
	 * HAVE TO BE OVER-RIDDEN IN SUB-CLASSES
	 */
	public void init()
	{
	}
	public void learn()
	{
	}
	public double eval(DataPoint p)
	{
		return -1.0;
	}
	public Ranker clone()
	{
		return null;
	}
	public String toString()
	{
		return "";
	}
	public String model()
	{
		return "[Not yet implemented]";
	}
	public void load(String fn)
	{
	}
	public void printParameters()
	{
	}
	public String name()
	{
		return "";
	}
}
