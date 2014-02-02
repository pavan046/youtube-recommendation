package assignment.similarity;

import java.util.HashSet;
import java.util.Set;

import assignment.model.YoutubeDescription;
/**
 * Merger because utilizes both categories and tags for calculating the similarity 
 * @author pavan
 *
 */
public class JaccardSimilarityMerger {
	private double alpha;
	/**
	 * the parameter alpha balances the optimal prominance for categories vs tags
	 * Equation -- alpha * jacsim(categories) + (1-alpha) * jacsim (tags)
	 * @param alpha -- between 0-1
	 */
	public JaccardSimilarityMerger(double alpha) {
		this.alpha = alpha; 
	}
	/**
	 * * Equation -- alpha * jacsim(categories) + (1-alpha) * jacsim (tags)
	 * @param one
	 * @param two
	 * @return
	 */
	public double getMergedSimilarity(YoutubeDescription one, YoutubeDescription two){
		double similarityValue = 0.0;
		JaccardSimilarity jacsim = new JaccardSimilarity(); 
		double catSim = jacsim.similarityCalculator(one.getCategories(), two.getCategories()); 
		double tagSim = jacsim.similarityCalculator(one.getTags(), two.getTags()); 
		similarityValue = alpha * catSim + (1-alpha) * tagSim;  
		return similarityValue; 
	}
	/**
	 * Calculates jaccard similarity
	 * @author pavan
	 *
	 */
	private class JaccardSimilarity{
		public double similarityCalculator(Set<String> one, Set<String> two){
			Set<String> intersection = new HashSet<String>();
			Set<String> union = new HashSet<String>();
			
			intersection.addAll(one); 
			intersection.retainAll(two);
				
			union.addAll(one);
			union.addAll(two);
			
			//Union 
			int unionSize = union.size();
			int intersectionSize = intersection.size();
			
			double value = (double)intersectionSize/unionSize; 
			
			return value; 
		}
	}
}
