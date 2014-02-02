package assignment.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import assignment.extractions.Extractor;
import assignment.extractions.VideoProcessor;
import assignment.extractions.ZemantaEntityExtractor;
import assignment.model.YoutubeDescription;
import assignment.parsers.JsonInputParser;
import assignment.utils.ConfigurationReader;

/**
 * The objective is to Tag the videos parsed from the 
 * json file
 * @author pavan
 *
 */
public class Tagger {
	private JsonInputParser jsonParser;
	private List<Extractor> extractors; 
	private String outputPathName, outputPrettyFile="data/outputTaggedViewableFile"; 
	private List<YoutubeDescription> videos; 
	private boolean tagged= false;
	private boolean inputFileTagged = false; 
	/**
	 * pathname of the json file
	 * @param pathName
	 */
	public Tagger(String inputPathName) {
		this.inputFileTagged = true; 
		jsonParser = new JsonInputParser(inputPathName, inputFileTagged); 
		extractors = new ArrayList<>(); 
	}
	
	/**
	 * pathname of the json file
	 * @param pathName
	 */
	public Tagger(String inputPathName, String outputJsonName, String outputFile) {
		this.inputFileTagged = false;
		jsonParser = new JsonInputParser(inputPathName, inputFileTagged); 
		extractors = new ArrayList<>(); 
		this.outputPathName = outputJsonName; 
		this.outputPrettyFile = outputFile; 
	}

	/**
	 * uses zemanta, however this can be made generic
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public void tag() {
		// Videos parsed 
		videos = jsonParser.parseJson(); 
		// Extractors for tagging 
		if (!inputFileTagged){
			Extractor<Set<String>> zemanta = new ZemantaEntityExtractor();
			extractors.add(zemanta); 
			VideoProcessor processor = new VideoProcessor(extractors); 
			processor.process(videos); 
			jsonParser.transformToJson(videos, outputPathName);
			writeOutputFile();
		}
		tagged = true; 
	}
	/**
	 * Writes a pretty output file of the Tagged videos
	 */
	private void writeOutputFile() {
		try {
			Writer write = new FileWriter(new File(outputPrettyFile));
			for(YoutubeDescription video: videos){
				write.append("=========================================\n");
				write.append(video.toString()+"\n");
				write.append("=========================================\n");
			}
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}

	/**
	 * Returns the tagged videos or null if videos are not present.
	 * @return
	 */
	public List<YoutubeDescription> getTaggedVideos(){
		if (tagged)
			return videos;
		else 
			return null; 
	}

	public static void main(String[] args) throws FileNotFoundException {
		ConfigurationReader configReader = new ConfigurationReader(args[0]); 
		Tagger tagger = new Tagger(configReader.getStringProperty("Tagger.inputFile"), 
				configReader.getStringProperty("Tagger.outputJsonFile"), 
				configReader.getStringProperty("Tagger.outputFile")); 
		tagger.tag();
	}
}
