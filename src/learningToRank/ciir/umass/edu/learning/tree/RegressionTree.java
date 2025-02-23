/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package learningToRank.ciir.umass.edu.learning.tree;

import java.util.ArrayList;
import java.util.List;

import learningToRank.ciir.umass.edu.learning.DataPoint;

/**
 * @author vdang
 */
public class RegressionTree {
	
	//Parameters
	protected int nodes = 10;//-1 for unlimited number of nodes (the size of the tree will then be controlled *ONLY* by minLeafSupport)
	protected int minLeafSupport = 1;
	
	//Member variables and functions 
	protected Split root = null;
	protected List<Split> leaves = null;
	
	protected DataPoint[] trainingSamples = null;
	protected double[] trainingLabels = null;
	protected int[] features = null;
	protected float[][] thresholds = null;
	protected int[] index = null;
	protected FeatureHistogram hist = null;
	
	public RegressionTree(Split root)
	{
		this.root = root;
		leaves = root.leaves();
	}
	public RegressionTree(int nLeaves, DataPoint[] trainingSamples, double[] labels, FeatureHistogram hist, int minLeafSupport)
	{
		this.nodes = nLeaves;
		this.trainingSamples = trainingSamples;
		this.trainingLabels = labels;
		this.hist = hist;
		this.minLeafSupport = minLeafSupport;
		index = new int[trainingSamples.length];
		for(int i=0;i<trainingSamples.length;i++)
			index[i] = i;
	}
	
	/**
	 * Fit the tree from the specified training data
	 */
	public void fit()
	{
		List<Split> queue = new ArrayList<Split>();
		root = new Split(index, hist, Float.MAX_VALUE, 0);
		root.setRoot(true);
		root.split(trainingLabels, minLeafSupport);
		insert(queue, root.getLeft());
		insert(queue, root.getRight());

		int taken = 0;
		while( (nodes == -1 || taken + queue.size() < nodes) && queue.size() > 0)
		{
			Split leaf = queue.get(0);
			queue.remove(0);
			
			if(leaf.getSamples().length < 2 * minLeafSupport)
			{
				taken++;
				continue;
			}
			
			if(!leaf.split(trainingLabels, minLeafSupport))//unsplitable (i.e. variance(s)==0; or after-split variance is higher than before)
				taken++;
			else
			{
				insert(queue, leaf.getLeft());
				insert(queue, leaf.getRight());
			}			
		}
		leaves = root.leaves();
	}
	
	/**
	 * Get the tree output for the input sample
	 * @param dp
	 * @return
	 */
	public double eval(DataPoint dp)
	{
		return root.eval(dp);
	}
	/**
	 * Retrieve all leave nodes in the tree
	 * @return
	 */
	public List<Split> leaves()
	{
		return leaves;
	}
	/**
	 * Clear samples associated with each leaves (when they are no longer necessary) in order to save memory
	 */
	public void clearSamples()
	{
		trainingSamples = null;
		trainingLabels = null;
		features = null;
		thresholds = null;
		index = null;
		hist = null;
		for(int i=0;i<leaves.size();i++)
			leaves.get(i).clearSamples();
	}
	
	/**
	 * Generate the string representation of the tree
	 */
	public String toString()
	{
		if(root != null)
			return root.toString();
		return "";
	}
	public String toString(String indent)
	{
		if(root != null)
			return root.toString(indent);
		return "";
	}
	
	public double variance()
	{
		double var = 0;
		for(int i=0;i<leaves.size();i++)
			var += leaves.get(i).getDeviance();
		return var;
	}

	protected void insert(List<Split> ls, Split s)
	{
		int i=0;
		while(i < ls.size())
		{
			if(ls.get(i).getDeviance() > s.getDeviance())
				i++;
			else
				break;
		}
		ls.add(i, s);
	}

}
