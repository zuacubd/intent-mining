/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package learningToRank.ciir.umass.edu.learning.neuralnet;

/**
 * @author vdang
 */
public class LogiFunction implements TransferFunction {
	
	@Override
	public double compute(double x)
	{
		return (double) (1.0 / (1.0 + Math.exp(-x)));
	}
	
	@Override
	public double computeDerivative(double x)
	{
		double output = compute(x);
		return (double) (output * (1.0 - output));
	}
}
