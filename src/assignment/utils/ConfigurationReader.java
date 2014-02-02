package assignment.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This Class reads the configuration 
 * from a properties file
 *  
 * @author pavan
 * 
 * The configuration reader can use a library 
 * However due to the licencing issues I am writing my own 
 *
 */
public class ConfigurationReader {
	public String configFile;
	private Map<String, String> properties = new HashMap<String, String>();
	/**
	 * The constructor loads the properties file
	 * @param propertiesFile
	 * @throws FileNotFoundException 
	 */
	public ConfigurationReader(String propertiesFile) throws FileNotFoundException{
		this.configFile = propertiesFile;
		loadConfigFile();
	}
	/**
	 * This generates a new configuration reader
	 * @param configReader
	 */
	public ConfigurationReader(ConfigurationReader configReader) {
		this.configFile = new String(configReader.configFile);
		this.properties = new HashMap<String, String>(configReader.properties);
	}
	/**
	 * Returns the property value as a string
	 * @param propertyName
	 * @return
	 */
	public String getStringProperty(String propertyName){
		return properties.get(propertyName);
	}
	/**
	 * Returns the property value as an Integer
	 * @param propertyName
	 * @return
	 */
	public Integer getIntegerProperty(String propertyName){
		return Integer.parseInt(properties.get(propertyName));
	}
	/**
	 * Returns the property value as a Double
	 * @param propertyName
	 * @return
	 */
	public Double getDoubleProperty(String propertyName){
		return Double.parseDouble(properties.get(propertyName));
	}
	/**
	 * Returns the property value as a Long
	 * @param propertyName
	 * @return
	 */
	public Long getLongProperty(String propertyName){
		return Long.parseLong(properties.get(propertyName));
	}
	/**
	 * Returns the boolean value of the property
	 * @param propertyName
	 * @return
	 */
	public boolean getBooleanProperty(String propertyName){
		return Boolean.parseBoolean(properties.get(propertyName));
	}
	
	private void loadConfigFile() throws FileNotFoundException{
		BufferedReader bufReader = new BufferedReader(new FileReader(new File(configFile)));
		String line = null;
		try {
			while((line=bufReader.readLine()) != null){
				if(line.startsWith("#") || line.isEmpty())
					continue;
				String prop[] = line.split("=");
				if(!prop[1].isEmpty())
					properties.put(prop[0], prop[1].trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Changes the property value.
	 * Specifically used when running the program for multiple users
	 * @param property
	 * @param value
	 */
	public void setProperty(String property, String value){
		properties.put(property, value);
	}
}
