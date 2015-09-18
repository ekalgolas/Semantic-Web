import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
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
	public static void main(final String[] args) {
		org.apache.log4j.Logger.getRootLogger()
			.setLevel(org.apache.log4j.Level.OFF);

		// some definitions
		final String personURI = "http://utdallas/Fall2015SemanticWeb/Keven";
		final String givenName = "Kevin";
		final String familyName = "Ates";
		final String prefix = "Dr.";
		final String middleName = "L.";
		final String fullName = prefix + " " + givenName + " " + middleName + " " + familyName;
		final String title = "Senior Lecturer";
		final String email = "mailto:atescomp@utdallas.edu";

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
		final Calendar calendar = new GregorianCalendar(1901, Calendar.JANUARY, 1);

		// create an empty Model
		final Model model = ModelFactory.createDefaultModel();

		// Create custom properties
		final Property middle = model.createProperty(VCARD.getURI(), "Middle");

		// create the resource
		// and add the properties cascading style
		@SuppressWarnings("unused")
		final Resource kevin = model.createResource(personURI)
			.addProperty(VCARD.FN, fullName)
			.addProperty(VCARD.N, model.createResource()
				.addProperty(VCARD.Prefix, prefix)
				.addProperty(VCARD.Given, givenName)
				.addProperty(middle, middleName)
				.addProperty(VCARD.Family, familyName))
			.addProperty(VCARD.BDAY, sdf.format(calendar.getTime()))
			.addProperty(VCARD.EMAIL, email)
			.addProperty(VCARD.TITLE, title);

		// Write model to different formats
		try {
			final FileWriter xmlWriter = new FileWriter("Lab1_2_EkalGolas.xml");
			final FileWriter ntpWriter = new FileWriter("Lab1_2_EkalGolas.ntp");
			final FileWriter n3Writer = new FileWriter("Lab1_2_EkalGolas.n3");

			model.write(xmlWriter, "RDF/XML");
			model.write(ntpWriter, "N-TRIPLE");
			model.write(n3Writer, "N3");
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}