import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.VCARD;

/**
 * Persist the graph in Jena using Dublin Core and TDB
 *
 * @author Ekal.Golas
 */
public class Lab_2 {
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
		final String bookURI = URI + "Book-";
		final String directorTitle = "director";
		final String authorTitle = "author";

		// Define Stanley Kubrick
		final String kubrickURI = personURI + "StanleyKubrick";
		final String kubrickGiven = "Stanley";
		final String kubrickFamily = "Kubrick";
		final String kubrickFullName = kubrickGiven + " " + kubrickFamily;

		// Define Author
		final String georgeURI = personURI + "PeterGeorge";
		final String georgeGiven = "Peter";
		final String georgeFamily = "George";
		final String georgeFullName = georgeGiven + " " + georgeFamily;

		// Define Red Alert
		final String redAlertURI = bookURI + "redAlert";
		final String redAlertTitle = "Red Alert";

		// Define Dr. Strangelove
		final String strangeloveURI = movieURI + "DrStrangelove";
		final String strangeloveTitle = "Dr. Strangelove";
		final String strangeloveYear = "1964";

		// Define A Clockwork Orange
		final String clockworkURI = movieURI + "AClockworkOrange";
		final String clockworkTitle = "A Clockwork Orange";
		final String clockworkYear = "1971";

		// Make a TDB-backed dataset
		final String directory = "MyDatabases/";
		final Dataset dataset = TDBFactory.createDataset(directory + "Dataset1");

		// create an empty Model
		final Model model = dataset.getNamedModel("myrdf");

		// Create classes
		final Resource movie = model.createResource(movieURI);
		final Resource person = model.createResource(personURI);
		final Resource book = model.createResource(bookURI);

		// Create custom properties
		final Property directorProperty = model.createProperty(movieURI, directorTitle);
		final Property adaptationOfProperty = model.createProperty(movieURI, "AdaptationOf");

		// Create the director
		// and add the properties cascading style
		final Resource kubrick = model.createResource(kubrickURI)
			.addProperty(RDF.type, person)
			.addProperty(VCARD.FN, kubrickFullName)
			.addProperty(VCARD.N, model.createResource()
				.addProperty(VCARD.Given, kubrickGiven)
				.addProperty(VCARD.Family, kubrickFamily))
			.addProperty(VCARD.TITLE, directorTitle);

		// Create the author
		final Resource george = model.createResource(georgeURI)
			.addProperty(RDF.type, person)
			.addProperty(VCARD.FN, georgeFullName)
			.addProperty(VCARD.N, model.createResource()
				.addProperty(VCARD.Given, georgeGiven)
				.addProperty(VCARD.Family, georgeFamily))
			.addProperty(VCARD.TITLE, authorTitle);

		// Create book
		final Resource redAlert = model.createResource(redAlertURI)
			.addProperty(RDF.type, book)
			.addProperty(DC.creator, george)
			.addProperty(DC.title, redAlertTitle);

		// Create movies
		final Resource clockwork = model.createResource(clockworkURI)
			.addProperty(RDF.type, movie)
			.addProperty(DC.title, clockworkTitle)
			.addProperty(DC.date, clockworkYear)
			.addProperty(directorProperty, kubrick)
			.addProperty(adaptationOfProperty, redAlert);

		final Resource strangelove = model.createResource(strangeloveURI)
			.addProperty(RDF.type, movie)
			.addProperty(DC.title, strangeloveTitle)
			.addProperty(DC.date, strangeloveYear)
			.addProperty(directorProperty, kubrick)
			.addProperty(adaptationOfProperty, redAlert);

		dataset.begin(ReadWrite.WRITE);
		try {
			dataset.commit();

			// Write model to different formats
			final FileWriter xmlWriter = new FileWriter("Lab2_3_EkalGolas.xml");
			final FileWriter n3Writer = new FileWriter("Lab2_3_EkalGolas.n3");

			model.write(xmlWriter, "RDF/XML");
			model.write(n3Writer, "N3");

			xmlWriter.close();
			n3Writer.close();
		} catch (final IOException e) {
			e.printStackTrace();
			dataset.end();
			model.close();
		}
	}
}