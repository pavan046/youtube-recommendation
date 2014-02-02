package assignment.extractions;

import java.util.ArrayList;
import java.util.List;
import assignment.model.YoutubeDescription;

/**
 * Pipeline class to call functors in sequence to extract information from Youtube descriptions.
 * 
 * @author pavan
 */
public class VideoProcessor {
	/**
	 * Method to run the information extraction process on tweets
	 * @param videos
	 * @param path 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	List<Extractor> extractors = new ArrayList<Extractor>();
	
	public VideoProcessor(List<Extractor> extractors) {
		this.extractors = extractors;
	}
	
	public void addExtractor(Extractor extrinstance) {
		extractors.add(extrinstance);
	}
	
	public List<YoutubeDescription> process(List<YoutubeDescription> videos) {
		List<YoutubeDescription> result = new ArrayList<YoutubeDescription>();
		for(YoutubeDescription video: videos){
			result.add(process(video));			
		}
		return result;
	}

	/*
	 * 1.get the video content (description, title)
	 * 2. Store it in the YoutubeDescription
	 * 
	 * 3.Pass it through all extractors. Examples are:
	 *     Entity Extractor (Zemanta)
	 *                (Store Entities in the Annotated Tweet)
	 */
	public YoutubeDescription process(YoutubeDescription video) {
		for (Extractor e: extractors) {
			e.process(video);
		}
		return video;
	}
	
}