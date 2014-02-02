package assignment.model;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a DS that comprises all the information of the 
 * Youtube videos 
 * 
 * @author pavan
 *
 */
public class YoutubeDescription {
	private int id; 
	private String description; 
	private String title; 
	private Set<String> categories = new HashSet<>(); 
	private Set<String> tags = new HashSet<>();
	/*
	 * Default getter and setter methods.  
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<String> getCategories() {
		return categories;
	}
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void setTags(Set<String> tags) {
		this.tags.addAll(tags);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/*
	 * Individual setter for sets
	 */
	public void setIndividualCategory(String category){
		this.categories.add(category); 
	}
	public void setIndividualTag(String tag){
		this.tags.add(tag); 
	}
	/**
	 * Overriding toString
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String output = "Description: " + this.description +"\n"; 
		output += "Title: " + this.title + "\n"; 
		output += "Categories: " + this.categories + "\n"; 
		output += "Tags: "+ this.tags; 
		return output;
	}
}
