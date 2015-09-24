import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;

/**
 * In-memory Jena model
 *
 * @author Ekal.Golas
 */
public class Answer2 {
	/**
	 * @param args
	 *            Command line arguments
	 */
	@SuppressWarnings("unused")
	public static void main(final String[] args) {
		org.apache.log4j.Logger.getRootLogger()
			.setLevel(org.apache.log4j.Level.OFF);

		// Some definitions
		final String URI = "http://utdallas/semclass#";
		final String movieURI = URI + "Movie-";
		final String personURI = URI + "Person-";
		final String directorTitle = "director";

		// Define Stanley Kubrick
		final String kubrickURI = personURI + "StanleyKubrick";
		final String kubrickGiven = "Stanley";
		final String kubrickFamily = "Kubrick";
		final String kubrickFullName = kubrickGiven + " " + kubrickFamily;

		// Define Dr. Strangelove
		final String strangeloveURI = movieURI + "drStrangelove";
		final String strangeloveTitle = "Dr. Strangelove";
		final String strangeloveYear = "1964";

		// Define A Clockwork Orange
		final String clockworkURI = movieURI + "aClockworkOrange";
		final String clockworkTitle = "A Clockwork Orange";
		final String clockworkYear = "1971";

		// create an empty Model
		final Model model = ModelFactory.createDefaultModel();

		// Create classes
		final Resource movie = model.createResource(movieURI);
		final Resource person = model.createResource(personURI);

		// Create custom properties
		final Property directorProperty = model.createProperty(movieURI, directorTitle);

		// Create the director
		// and add the properties cascading style
		final Resource kubrick = model.createResource(kubrickURI)
			.addProperty(RDF.type, person)
			.addProperty(VCARD.FN, kubrickFullName)
			.addProperty(VCARD.N, model.createResource()
				.addProperty(VCARD.Given, kubrickGiven)
				.addProperty(VCARD.Family, kubrickFamily))
			.addProperty(VCARD.TITLE, directorTitle);

		// Create movies
		final Resource clockwork = model.createResource(clockworkURI)
			.addProperty(RDF.type, movie)
			.addProperty(DC.title, clockworkTitle)
			.addProperty(DC.date, clockworkYear)
			.addProperty(directorProperty, kubrick);

		final Resource strangelove = model.createResource(strangeloveURI)
			.addProperty(RDF.type, movie)
			.addProperty(DC.title, strangeloveTitle)
			.addProperty(DC.date, strangeloveYear)
			.addProperty(directorProperty, kubrick);

		model.close();
	}
}