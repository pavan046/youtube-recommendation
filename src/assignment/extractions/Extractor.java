package assignment.extractions;

import java.io.Serializable;

import assignment.model.YoutubeDescription;

/**
 * 
 * @author pavan
 * 
 * Interface for all the extractions.
 * @param <E>
 */
public interface Extractor<E> {

	/**
	 * Performs information extraction on some raw text.
	 * @param text - input with raw text
	 * @return annotations - extracted information from raw input
	 */
	public E extract(Object text); 
	/**
	 * Updates the video description with the extracted information from the tweet text
	 * @param video
	 */
	public void process(YoutubeDescription video);

}
