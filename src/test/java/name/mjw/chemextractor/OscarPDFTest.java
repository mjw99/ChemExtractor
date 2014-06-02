package name.mjw.chemextractor;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class OscarPDFTest {

	private static OscarPDF2JSON oscarPDF;

	@BeforeClass
	public static void setUp() {
		String pdfFileName = null;

		try {
			pdfFileName = URLDecoder.decode(
					OscarPDFTest.class.getResource(
							"/name/mjw/chemextractor/chem_sample_patents/US20110004037.pdf").getFile(),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		oscarPDF = new OscarPDF2JSON(pdfFileName);
	}

	@Test
	public void getJSONtoFile() {

		PrintWriter writer = null;

		try {
			writer = new PrintWriter("US20110004037.json", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(oscarPDF.getJSON());

		writer.close();

	}

	@Ignore
	@Test
	public void getUniqueStandardInChIs() {

		Set<String> expectedResults = new HashSet<String>(Arrays.asList(
						"InChI=1S/C8H8/c1-2-8-6-4-3-5-7-8/h2-7H,1H2",
						"InChI=1S/C6H6O/c7-6-4-2-1-3-5-6/h1-5,7H",
						"InChI=1S/C",
						"InChI=1S/C4H10/c1-3-4-2/h3-4H2,1-2H3",
						"InChI=1S/C8H8O/c1-7(9)8-5-3-2-4-6-8/h2-6H,1H3",
						"InChI=1S/C8H6/c1-2-8-6-4-3-5-7-8/h1,3-7H",
						"InChI=1S/C6H6/c1-2-4-6-5-3-1/h1-6H",
						"InChI=1S/H2/h1H",
						"InChI=1S/C7H6O/c8-6-7-4-2-1-3-5-7/h1-6H",
						"InChI=1S/K.H",
						"InChI=1S/H2O/h1H2",
						"InChI=1S/C8H10/c1-2-8-6-4-3-5-7-8/h3-7H,2H2,1H3",
						"InChI=1S/O",
						"InChI=1S/C7H8/c1-7-5-3-2-4-6-7/h2-6H,1H3"
		));

		Set<String> results = oscarPDF.getUniqueStandardInChIs();
		for (String item : results) {
			System.out.println(item);
		}

		assertEquals(expectedResults, results);

	}

	@Test
	public void getUniqueStandardInChIKeys() {

		Set<String> expectedResults = new HashSet<String>(Arrays.asList(
				"YNQLUTRBYVCPMQ-UHFFFAOYSA-N",
				"ISWSIDIOOBJBQZ-UHFFFAOYSA-N",
				"IJDNQMDRQITEOD-UHFFFAOYSA-N",
				"UEXCJVNBTNXOEH-UHFFFAOYSA-N",
				"XLYOFNOQVPJJNP-UHFFFAOYSA-N",
				"HUMNYLRZRPPJDN-UHFFFAOYSA-N",
				"YXFVVABEGXRONW-UHFFFAOYSA-N",
				"KWOLFJPFCHCOCG-UHFFFAOYSA-N",
				"PPBRXRYQALVLMV-UHFFFAOYSA-N",
				"NTTOTNSKUYCDAV-UHFFFAOYSA-N",
				"QVGXLLKOCUKJST-UHFFFAOYSA-N",
				"OKTJSMMVPCPJKN-UHFFFAOYSA-N",
				"UFHFLCQGNIYNRP-UHFFFAOYSA-N",
				"UHOVQNZJYSORNB-UHFFFAOYSA-N"
		));

		Set<String> results = oscarPDF.getUniqueStandardInChIKeys();
		for (String item : results) {
			System.out.println(item);
		}

		assertEquals(expectedResults, results);

	}

	@Test
	public void checkMD5Sum() {

		assertEquals("344501ec7dbd6d351a0520e8c750e903", oscarPDF.getMd5Sum());

	}

}
