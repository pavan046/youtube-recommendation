package assignment.extractions;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import assignment.model.YoutubeDescription;

import com.zemanta.api.Zemanta;
import com.zemanta.api.ZemantaResult;
import com.zemanta.api.suggest.Markup;
import com.zemanta.api.suggest.Markup.Link;
import com.zemanta.api.suggest.Markup.Target;
import com.zemanta.api.suggest.Markup.TargetType;
/**
 * This class calls the Zemanta API 
 * @author pavan
 *
 */
public class ZemantaEntityExtractor implements Extractor<Set<String>> {
	private Log LOG = new LogFactoryImpl().getInstance(ZemantaEntityExtractor.class);

	@Override
	public Set<String> extract(Object text) {
		LOG.debug("Calling the API for :" + (String)text);
		Set<String> entities = new HashSet<>();
		final String API_SERVICE_URL = "http://api.zemanta.com/services/rest/0.0/";
		String apiKey = "ggvv9t1wbjj3mtsbicioqcdy";
		// Parameters (not url encoded! they get encoded in Zemanta object)
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("method", "zemanta.suggest");
		parameters.put("api_key", apiKey);
		parameters.put("text", (String)text);
		parameters.put("format", "xml");

		Zemanta zem = new Zemanta(apiKey, API_SERVICE_URL);	

		// Example 1: suggest
		ZemantaResult zemResult = zem.suggest(parameters);
		if(!zemResult.isError) {
			//System.out.println(zemResult);
			Markup markup = zemResult.markup;
			List<Link> links = markup.links;
			for(Link link: links){
				//System.out.println(link);
				for(Target target: link.targets){
					if(target.type.equals(TargetType.WIKIPEDIA))
						entities.add(target.title);
				}
			}
		}
		return entities;
	}

	@Override
	public void process(YoutubeDescription video) {
		//TODO:  This is a hack -- I am annotating both description and title together. 
		//TODO: Not sure whether categories should also be included for tagging
		video.setTags(extract(video.getDescription()+" "+video.getTitle()));
		LOG.info( "Extracted " + video.getTags().size() + " entities");
			
	}

	public static void main(String[] args) {
		Extractor zemantaExtractor = new ZemantaEntityExtractor();
		String text = "Boehner explicitly threatens default, and global economic collapse, if Obama won't give the House GOP what it wants";
		System.out.println(zemantaExtractor.extract(text));
	}

}
