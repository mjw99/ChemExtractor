package name.mjw.chemextractor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;

public class OscarPDFTest {

	private static OscarPDF2JSON oscarPDF;

	@Before
	public void setUp() {
		String pdfFileName = null;

		try {
			pdfFileName = URLDecoder.decode(
					getClass().getResource(
							"/name/mjw/chemextractor/US8680279.pdf").getFile(),
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
			writer = new PrintWriter("example.json", "UTF-8");
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
	public void getInchi() {

		oscarPDF.printInchi();
	}

	@Test
	public void getCML() {

		oscarPDF.printCML();
	}

	@Test
	public void checkMD5Sum() {

		try {
			String pdfFileName = URLDecoder.decode(
					getClass().getResource(
							"/name/mjw/chemextractor/US8680279.pdf").getFile(),
					"utf-8");

			OscarPDF2JSON oscarPDF = new OscarPDF2JSON();

			System.out.println(oscarPDF.getMd5Sum(pdfFileName));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
