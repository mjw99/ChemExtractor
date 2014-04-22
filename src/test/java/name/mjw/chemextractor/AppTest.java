package name.mjw.chemextractor;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.junit.Test;

public class AppTest {

	@Test
	public void processStringWithOscar() {
		App app = new App();

		app.processStringWithOscarToObtainInchi("Benzene, car, television, phenol");
	}

	
	
	@Test
	public void processPDF() {
		App app = new App();
		
		try {
			String pdfFileName = URLDecoder.decode(getClass().getResource("/name/mjw/chemextractor/US8680279.pdf").getFile(),"utf-8");
			app.processPDFfromFileName(pdfFileName);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		

	}
}
