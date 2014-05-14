package name.mjw.chemextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

	// TODO
	String md5SumOfPDFFile = "abc";

	PDDocument doc = null;

	/**
	 * Extrated text from the PDF
	 */
	String[] textLinesWithinPDF = null;

	/**
	 * Oscar4 istance for processing
	 */
	Oscar oscar = new Oscar();

	/**
	 * ResolvedNamedEntity found by Oscar
	 */
	List<ResolvedNamedEntity> entities = null;

	/**
	 * ChemicalData are unique as a function of name
	 */
	HashMap<String, ChemicalDatum> chemicalData = new HashMap<String, ChemicalDatum>();

	public OscarPDF2JSON() {

	}

	public OscarPDF2JSON(String pdfFileName) {

		// Pull only the text out of a PDF as a String
		extractTextfromPDFFileName(pdfFileName);

		// Process the text string with OSCAR
		generateEntities();

		// Generate a unique set of chemicals from the PDF
		this.chemicalData = populateChemicalData();
	}

	public OscarPDF2JSON(FileInputStream is) {

		extractTextfromPDFStream(is);

		// Process the text string with OSCAR
		generateEntities();

		// Generate a unique set of chemicals from the PDF
		this.chemicalData = populateChemicalData();
	}

	/**
	 * Creates a PdfJSON, adds ChemicalData to it and then returns a JSON
	 * representaion of PdfJSON.
	 * 
	 * @return JSON String of chemicals
	 */
	public String getJSON() {
		Gson gson = new Gson();

		PdfJSON pdfJSON = new PdfJSON();

		pdfJSON.chemicalData = chemicalData;

		// TODO
		pdfJSON.setMd5Sum(md5SumOfPDFFile);

		return gson.toJson(pdfJSON);

	}

	/**
	 * Creates a PdfJSON, adds ChemicalData to it and then returns the PdfJSON
	 * representaion of PdfJSON.
	 * 
	 * @return PdfJSON
	 */
	public PdfJSON getPdfJSON() {

		PdfJSON pdfJSON = new PdfJSON();

		pdfJSON.chemicalData = chemicalData;

		// TODO
		pdfJSON.setMd5Sum(md5SumOfPDFFile);

		return pdfJSON;

	}

	/**
	 * Forms ChemicalData set containing unique chemical entities found in the
	 * pdf.
	 * 
	 * @return chemicalData of the PDF
	 */
	HashMap<String, ChemicalDatum> populateChemicalData() {

		HashMap<String, ChemicalDatum> chemicalData = new HashMap<String, ChemicalDatum>();

		for (ResolvedNamedEntity ne : entities) {
			ChemicalStructure inchi = ne
					.getFirstChemicalStructure(FormatType.INCHI);

			if (inchi != null) {

				ChemicalDatum cd = new ChemicalDatum();

				cd.setName(ne.getSurface());
				cd.setStandardInChIFromInChI(inchi.getValue());

				chemicalData.put(ne.getSurface(), cd);
			}

		}

		return chemicalData;

	}

	/**
	 * For every member of textLinesWithinPDF[], use Oscar to find and resolved
	 * Named Entites, adding them to entities
	 */
	void generateEntities() {

		entities = new ArrayList<ResolvedNamedEntity>();

		for (int i = 0; i < textLinesWithinPDF.length; i++) {

			entities.addAll(oscar
					.findAndResolveNamedEntities(textLinesWithinPDF[i]));

		}

	}

	public void printStandardInChI() {

		Iterator<?> it = this.chemicalData.entrySet().iterator();

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, ChemicalDatum> pairs = (Map.Entry<String, ChemicalDatum>) it
					.next();
			// System.out.println(pairs.getKey() + " = " +
			// pairs.getValue().getStandardInChI());
			System.out.println(pairs.getValue().getStandardInChI());
			it.remove(); // avoids a ConcurrentModificationException
		}

	}

	public void printStandardInChIKeys() {

		Iterator<?> it = this.chemicalData.entrySet().iterator();

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, ChemicalDatum> pairs = (Map.Entry<String, ChemicalDatum>) it
					.next();
			System.out.println(pairs.getValue().getStandardInchiKey());
			it.remove(); // avoids a ConcurrentModificationException
		}

	}

	public void printCML() {

		Iterator<?> it = this.chemicalData.entrySet().iterator();

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, ChemicalDatum> pairs = (Map.Entry<String, ChemicalDatum>) it
					.next();
			System.out.println(pairs.getValue().getCml());
			it.remove(); // avoids a ConcurrentModificationException
		}

	}

	public void extractTextfromPDFFileName(String fileName) {

		extractTextfromPDFStream(getPDFStreamFromFileName(fileName));

	}

	public void processfromPDFStream(FileInputStream is) {

		extractTextfromPDFStream(is);

		// Process the text string with OSCAR
		generateEntities();

		// Generate a unique set of chemicals from the PDF
		this.chemicalData = populateChemicalData();

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
