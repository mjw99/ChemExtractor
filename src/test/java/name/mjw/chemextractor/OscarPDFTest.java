package name.mjw.chemextractor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;

public class OscarPDFTest {

	private static OscarPDF oscarPDF;

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

		oscarPDF = new OscarPDF(pdfFileName);
	}

	@Test
	public void getInchi() {

		oscarPDF.getInchi();
	}

	@Test
	public void getCML() {

		oscarPDF.getCML();
	}

	@Test
	public void checkMD5Sum() {

		try {
			String pdfFileName = URLDecoder.decode(
					getClass().getResource(
							"/name/mjw/chemextractor/US8680279.pdf").getFile(),
					"utf-8");

			OscarPDF oscarPDF = new OscarPDF();

			System.out.println(oscarPDF.getMd5Sum(pdfFileName));

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

}
