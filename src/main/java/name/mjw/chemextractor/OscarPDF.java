package name.mjw.chemextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ChemicalStructure;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.FormatType;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ResolvedNamedEntity;

public class OscarPDF {

	
	Oscar oscar = null;
	PDDocument doc = null;
	String textOfPDF = null;
	
	String md5Sum = null;

	OscarPDF() {
		oscar = new Oscar();
	}

	OscarPDF(String fileName) {
		oscar = new Oscar();
		processPDFfromFilename(fileName);
	}

	public void getInchi() {

		List<ResolvedNamedEntity> entities = oscar.findAndResolveNamedEntities(textOfPDF);

		for (ResolvedNamedEntity ne : entities) {

			// System.out.println(ne.getSurface());
			ChemicalStructure inchi = ne.getFirstChemicalStructure(FormatType.INCHI);


			if (inchi != null) {
				//System.out.println(ne.getSurface() + inchi);
				System.out.println(inchi);
			}

		}
	}

	public void getCML() {

		List<ResolvedNamedEntity> entities = oscar
				.findAndResolveNamedEntities(textOfPDF);
		for (ResolvedNamedEntity ne : entities) {

			ChemicalStructure cml = ne
					.getFirstChemicalStructure(FormatType.CML);
			if (cml != null) {
				System.out.println(cml);
			}

		}
	}
	
	
	public void processPDFfromFilename(String fileName) {
		
		processPDFfromStream(getPDFStreamFromFileName(fileName));
		
	}

	void processPDFfromStream(FileInputStream is) {

		try {
			doc = PDDocument.load(is);

			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PDFTextStripper stripper = null;
		try {
			stripper = new PDFTextStripper();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			textOfPDF = stripper.getText(doc);
			doc.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	FileInputStream getPDFStreamFromFileName(String fileName) {

		File file = new File(fileName);
		FileInputStream is = null;

		try {
			is = new FileInputStream(file);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return is;

	}

	
	String getMd5SumFromStream(FileInputStream is) {

		String md5Sum = null;
		try {
			md5Sum =  DigestUtils.md5Hex(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return md5Sum;

	}
	
	public String getMd5Sum(String fileName){
		
		return getMd5SumFromStream(getPDFStreamFromFileName(fileName));
		
	}

}
