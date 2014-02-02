package assignment.parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import assignment.model.*;

/**
 * This class is used to parse the input json of the videos 
 * 
 * @author pavan
 *
 */
public class JsonInputParser {
	private String inputFilePath;
	private boolean tagged; 
	/**
	 * 
	 * @param inputFilePath -- path of the json input file
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public JsonInputParser(String inputFilePath, boolean tagged) {
		this.inputFilePath = inputFilePath; 
		this.tagged = tagged; 
	}
	/**
	 * Parses the JSON and returns a list of Youtube video descriptions 
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public List<YoutubeDescription> parseJson() {
		StringBuffer jsonString = new StringBuffer(); 
		List<YoutubeDescription> youtubeDescriptions = new ArrayList<>(); 
		try{
			BufferedReader readFile = new BufferedReader(new FileReader(new File(inputFilePath))); 
			String line = null; 
			while((line = readFile.readLine()) != null)
				jsonString.append(line); 
			JSONArray youtubeDescriptionsArray = new JSONArray(jsonString.toString()); 
			for(int i=0; i<youtubeDescriptionsArray.length(); i++){
				YoutubeDescription video = new YoutubeDescription(); 
				JSONObject youtubeDescriptionJson = youtubeDescriptionsArray.getJSONObject(i); 
				video.setId(i+1); 
				video.setDescription(youtubeDescriptionJson.getString("description")); 
				video.setTitle(youtubeDescriptionJson.getString("title"));
				JSONArray categoriesJson = youtubeDescriptionJson.getJSONArray("categories"); 
				for(int j=0; j <categoriesJson.length(); j++)
					video.setIndividualCategory(categoriesJson.getString(j)); 
				// If the output already contains tagged json
				if(tagged){
					JSONArray tagsJson = youtubeDescriptionJson.getJSONArray("tags"); 
					for(int j=0; j<tagsJson.length(); j++)
						video.setIndividualTag(tagsJson.getString(j));
				}
				youtubeDescriptions.add(video); 
			}
		} catch (IOException e){
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return youtubeDescriptions; 
	}

	public void transformToJson(List<YoutubeDescription> videos, String outputPath){
		String json = null; 
		try {
			Writer write = new FileWriter(new File(outputPath));
			JSONArray videosArray = new JSONArray(); 
			for(YoutubeDescription video: videos){
				JSONObject jsonVideo = new JSONObject(); 

				jsonVideo.put("title", video.getTitle());
				jsonVideo.put("description", video.getDescription());
				jsonVideo.put("categories", video.getCategories()); 
				jsonVideo.put("tags", video.getTags()); 

				videosArray.put(jsonVideo); 
			}
			write.append(videosArray.toString()); 
			write.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public static void main(String[] args) {
		JsonInputParser parser = new JsonInputParser("data/outputTags.json", true); 
		List<YoutubeDescription> videos = parser.parseJson(); 
		parser.transformToJson(videos, "data/output.json");
		for(YoutubeDescription video: videos){
			System.out.println(video.toString());
		}
	}
}
