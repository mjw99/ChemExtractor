package name.mjw.chemextractor;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.BeforeClass;
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
			writer = new PrintWriter("US8680279.json", "UTF-8");
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

	@Test
	public void printStandardInChI() {

		oscarPDF.printStandardInChI();
	}

	@Test
	public void printStandardInChIKeys() {

		oscarPDF.printStandardInChIKeys();
	}

	@Test
	public void checkMD5Sum() {

		assertEquals("344501ec7dbd6d351a0520e8c750e903", oscarPDF.getMd5Sum());

	}

}
