package test;

import java.util.Iterator;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test TDB persistence
 *
 * @author Ekal.Golas
 */
public class TDBTest {
	/**
	 * Setup before running tests
	 */
	@Before
	public void setup() {
		org.apache.log4j.Logger.getRootLogger()
			.setLevel(org.apache.log4j.Level.OFF);
	}

	/**
	 * Test to check if model was persisted properly
	 */
	@Test
	public void testTDBCreation() {
		// Open a TDB-backed dataset
		final String directory = "MyDatabases/";
		final Dataset dataset = TDBFactory.createDataset(directory + "Dataset1");

		// Some definitions
		final String personURI = "http://utdallas/Fall2015SemanticWeb/Keven";
		final String givenName = "Kevin";
		final String familyName = "Ates";
		final String prefix = "Dr.";
		final String middleName = "L.";
		final String fullName = prefix + " " + givenName + " " + middleName + " " + familyName;

		dataset.begin(ReadWrite.READ);
		try {
			// Test if named model was created and persisted
			final Iterator<String> graphNames = dataset.listNames();
			final String graphName = graphNames.next();
			Assert.assertNotNull("No named model found", graphName);

			// Get model inside the transaction
			final Model model = dataset.getNamedModel(graphName);
			final Resource resource = model.getResource(personURI);
			Assert.assertNotNull("Resource not found", resource);

			// Check if property contains the value as intended
			final Statement statement = resource.getProperty(VCARD.FN);
			Assert.assertEquals("Data not matched", fullName, statement.getString());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}