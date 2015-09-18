import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.VCARD;

/**
 * Jena RDF tutorial
 *
 * @author Ekal.Golas
 */
public class Answer1 {
	/**
	 * @param args
	 *            Command line arguments
	 */
	public static void main(final String[] args) {
		org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.OFF);

		// some definitions
		final String personURI = "http://somewhere/JohnSmith";
		final String givenName = "John";
		final String familyName = "Smith";
		final String fullName = givenName + " " + familyName;

		// create an empty Model
		final Model model = ModelFactory.createDefaultModel();

		// create the resource
		// and add the properties cascading style
		@SuppressWarnings("unused")
		final Resource johnSmith = model.createResource(personURI)
			.addProperty(VCARD.FN, fullName)
			.addProperty(VCARD.N, model.createResource()
				.addProperty(VCARD.Given, givenName)
				.addProperty(VCARD.Family, familyName));

		// list the statements in the Model
		final StmtIterator iter = model.listStatements();

		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			final Statement stmt = iter.nextStatement(); // get next statement
			final Resource subject = stmt.getSubject(); // get the subject
			final Property predicate = stmt.getPredicate(); // get the predicate
			final RDFNode object = stmt.getObject(); // get the object

			System.out.print(subject.toString());
			System.out.print(" " + predicate.toString() + " ");
			if (object instanceof Resource) {
				System.out.print(object.toString());
			} else {
				// object is a literal
				System.out.print(" \"" + object.toString() + "\"");
			}

			System.out.println(" .");
		}
	}
}