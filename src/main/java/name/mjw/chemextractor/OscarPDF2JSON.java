package name.mjw.chemextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.google.gson.Gson;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ChemicalStructure;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.FormatType;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ResolvedNamedEntity;

/**
 * Proof of concept example to extract chemical data from a PDF file and return
 * it in a JSON format.
 * 
 * @author mw529
 * 
 */
public class OscarPDF2JSON {

	String md5SumOfPDFFile = null;

	PDDocument doc = null;
	
	/**
	 * Extrated text from the PDF
	 */
	String[] textLinesWithinPDF = null;

	Oscar oscar = null;
	List<ResolvedNamedEntity> entities = null;

	
	public OscarPDF2JSON() {
		oscar = new Oscar();
	}

	public OscarPDF2JSON(String pdfFileName) {
		oscar = new Oscar();
		
		// Pull only the text out of a PDF as a String
		extractTextfromPDFFileName(pdfFileName);
		
		// Process the text string with OSCAR
		generateEntities();
	}
	
	public OscarPDF2JSON(FileInputStream is) {
		oscar = new Oscar();

		extractTextfromPDFStream(is);
		
		// Process the text string with OSCAR
		generateEntities();
	}

	/**
	 * Populates a PdfJSON instance with ChemicalData
	 * @return JSON String of chemicals
	 */
	public String getJSON() {
		Gson gson = new Gson();

		PdfJSON pdfJSON = new PdfJSON();

		pdfJSON.chemicalData = populateChemicalData();

		// TODO
		pdfJSON.setMd5Sum("abc");

		return gson.toJson(pdfJSON);

	}

	HashMap<String, ChemicalDatum> populateChemicalData() {

		HashMap<String, ChemicalDatum> chemicalData = new HashMap<String, ChemicalDatum>();

		for (ResolvedNamedEntity ne : entities) {
			ChemicalStructure inchi = ne
					.getFirstChemicalStructure(FormatType.INCHI);

			if (inchi != null) {

				ChemicalDatum cd = new ChemicalDatum();

				cd.setName(ne.getSurface());
				cd.setInchi(inchi.getValue());

				chemicalData.put(ne.getSurface(), cd);
			}

		}

		return chemicalData;

	}

	/**
	 * For every member of textLinesWithinPDF[], use Oscar
	 * to find and resolved Named Entites, adding them to 
	 * entities
	 */
	void generateEntities() {

		entities = new ArrayList<ResolvedNamedEntity>();

		for (int i = 0; i < textLinesWithinPDF.length; i++) {

			entities.addAll(oscar
					.findAndResolveNamedEntities(textLinesWithinPDF[i]));

		}

	}

	public void printInchi() {

		for (ResolvedNamedEntity ne : entities) {

			ChemicalStructure inchi = ne
					.getFirstChemicalStructure(FormatType.INCHI);

			if (inchi != null) {

				System.out.println(inchi.getValue());

			}

		}
	}

	public void printCML() {

		for (ResolvedNamedEntity ne : entities) {

			ChemicalStructure cml = ne
					.getFirstChemicalStructure(FormatType.CML);
			if (cml != null) {
				System.out.println(cml);
			}

		}
	}

	public void extractTextfromPDFFileName(String fileName) {

		extractTextfromPDFStream(getPDFStreamFromFileName(fileName));

	}

	void extractTextfromPDFStream(FileInputStream is) {

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
			// Split the string into lines by new line.
			textLinesWithinPDF = stripper.getText(doc).split("\\r?\\n");
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
			md5Sum = DigestUtils.md5Hex(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return md5Sum;

	}

	public String getMd5Sum(String fileName) {

		return getMd5SumFromStream(getPDFStreamFromFileName(fileName));

	}

}
