package assignments.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import assignments.utils.ConfigurationReader;
import assignments.utils.ValueComparator;

import assignment.model.YoutubeDescription;
import assignment.similarity.JaccardSimilarityMerger;

/**
 * The objective is recommendation given some videos 
 * with categories and tags 
 * 
 * If tags are not present tags are extracted
 * @author pavan
 *
 */
public class Recommender {
	private Tagger tagger; 
	List<YoutubeDescription> videos; 
	JaccardSimilarityMerger similarityMeasurer;
	Map<String, YoutubeDescription> videoTitleMap; 
	Map<String, Double> recommendedVideos; 
	/**
	 * The constructor requires the following if the videos are not yet tagged
	 * @param inputFile -- Json input of videos
	 * @param outputJsonFile -- Json outut of videos with Tag 
	 * @param similarityFactor -- Learning factor between categories and Tagging
	 */
	public Recommender(String inputFile, String outputTaggedFile, 
			String outputJsonFile, String outputRecommendedFile, double similarityFactor) {
		videos = new ArrayList<>(); 
		tagger = new Tagger(inputFile, outputJsonFile, outputTaggedFile);
		similarityMeasurer = new JaccardSimilarityMerger(similarityFactor);
		videoTitleMap = new HashMap<String, YoutubeDescription>();
	}
	
	/**
	 * 
	 * @param inputTaggedFile
	 * @param outputFile
	 * @param similarityFactor
	 */
	public Recommender(String inputTaggedFile, String outputFile, 
			double similarityFactor) {
		videos = new ArrayList<>(); 
		tagger = new Tagger(inputTaggedFile);
		similarityMeasurer = new JaccardSimilarityMerger(similarityFactor);
		videoTitleMap = new HashMap<String, YoutubeDescription>();
	}

	public List<YoutubeDescription> recommend(String videoTitle, int topK, String outputFile){
		recommendedVideos = new HashMap<>();
		List<YoutubeDescription> topVideos = new ArrayList<>(); 
		// If not parsed then parse and tag the video
		if(videos.isEmpty()){
			tagger.tag(); 
			videos = tagger.getTaggedVideos(); 
		}
		//Construct the map 
		for(YoutubeDescription simVideo: videos)
			videoTitleMap.put(simVideo.getTitle(), simVideo); 

		YoutubeDescription videoForRecommendation = videoTitleMap.get(videoTitle); 

		// Get Similarity Score 
		for(YoutubeDescription simVideo: videos){
			if(!simVideo.getTitle().equalsIgnoreCase(videoForRecommendation.getTitle()))
				recommendedVideos.put(simVideo.getTitle(),
						similarityMeasurer.getMergedSimilarity(videoForRecommendation, simVideo));
		}
		
		// Sort the Map according to the score
		ValueComparator bvc =  new ValueComparator(recommendedVideos);
		TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
		sorted_map.putAll(recommendedVideos);
		
		int i=1;
		for(String title: sorted_map.keySet()){
			if(i > topK)
				break;
			topVideos.add(videoTitleMap.get(title)); 
			i++;
		}
		writeOutput(outputFile, sorted_map, topK); 
		return topVideos; 
	}
	
	/**
	 * Writes the output to a file 
	 * @param outputFile
	 * @param sorted_map
	 */
	private void writeOutput(String outputFile,
			TreeMap<String, Double> sorted_map, int topK) {
		Writer write;
		try {
			write = new FileWriter(new File(outputFile));
			int i=1;
			for(String title: sorted_map.keySet()){
				if(i > topK)
					break;
				write.append("=============== Recommended Video "+ i + " ============\n");
				write.append(videoTitleMap.get(title).toString() + "\n");
				write.append("Relevancy Score: " + recommendedVideos.get(title).toString() + "\n");
				write.append("=============================================\n");
				i++;
			}
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) throws FileNotFoundException {
		ConfigurationReader configReader = new ConfigurationReader(args[0]);
		Recommender recommender; 
		if(configReader.getBooleanProperty("Recommender.alreadyTagged")){
			recommender = new Recommender(configReader.getStringProperty("Tagger.outputJsonFile"), 
					configReader.getStringProperty("Recommender.outputFile"), 
					configReader.getDoubleProperty("Recommender.similarityFactor"));
		}
		else
			recommender = new Recommender(configReader.getStringProperty("Tagger.inputFile"), 
					configReader.getStringProperty("Tagger.outputJsonFile"), 
					configReader.getStringProperty("Tagger.outputFile"), 
					configReader.getStringProperty("Recommender.outputFile"), 
					configReader.getDoubleProperty("Recommender.similarityFactor")); 
		System.out.println("Title: " + configReader.getStringProperty("Recommender.videotitle"));
		List<YoutubeDescription> recommendedVideos = recommender.recommend(
					configReader.getStringProperty("Recommender.videotitle"), 
					configReader.getIntegerProperty("Recommender.topk"), 
					configReader.getStringProperty("Recommender.outputFile"));
		System.out.println("Recommendations available on File: " + 
					configReader.getStringProperty("Recommender.outputFile"));
	}
}
