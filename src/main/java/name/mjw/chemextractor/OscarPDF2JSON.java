package name.mjw.chemextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

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

	/**
	 * MD5SUM hash of the PDF.
	 */
	String md5SumOfPDFFile = null;

	/**
	 * Core PDFBOX representation of a PDF document.
	 */
	PDDocument doc = null;

	/**
	 * All extracted text from the PDF via PDFBOX, split into string array
	 * entries as a function of new line.
	 */
	String[] textLinesWithinPDF = null;

	/**
	 * OSCAR4 instance for processing
	 */
	Oscar oscar = new Oscar();

	/**
	 * ResolvedNamedEntities found by OSCAR.
	 */
	List<ResolvedNamedEntity> resolvedNamedEntities = null;

	/**
	 * ChemicalData are unique as a function of name.
	 * 
	 * TODO; duplication can occur if two entries are the same chemicals but
	 * have different names.
	 */
	HashMap<String, ChemicalDatum> chemicalData = new HashMap<>();

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
	 * Representation of PdfJSON.
	 * 
	 * @return JSON String of chemicals
	 */
	public String getJSON() {
		PdfJSON pdfJSON = new PdfJSON();

		pdfJSON.setChemicalData(chemicalData);

		pdfJSON.setMd5Sum(this.md5SumOfPDFFile);

		Gson gson = new Gson();

		return gson.toJson(pdfJSON);
	}

	/**
	 * Creates a PdfJSON, adds ChemicalData to it and then returns the PdfJSON
	 * Representation of PdfJSON.
	 * 
	 * @return PdfJSON
	 */
	public PdfJSON getPdfJSON() {

		PdfJSON pdfJSON = new PdfJSON();

		pdfJSON.setChemicalData(chemicalData);

		pdfJSON.setMd5Sum(this.md5SumOfPDFFile);

		return pdfJSON;
	}

	/**
	 * Forms ChemicalData set containing unique chemical entities found in the
	 * PDF.
	 * 
	 * @return chemicalData of the PDF
	 */
	HashMap<String, ChemicalDatum> populateChemicalData() {

		HashMap<String, ChemicalDatum> chemicalData = new HashMap<>();

		for (ResolvedNamedEntity ne : resolvedNamedEntities) {
			ChemicalStructure stdInchi = ne.getFirstChemicalStructure(FormatType.STD_INCHI);
			ChemicalStructure stdInchIKey = ne.getFirstChemicalStructure(FormatType.STD_INCHI_KEY);

			if (stdInchi != null) {

				ChemicalDatum cd = new ChemicalDatum();

				cd.setName(ne.getSurface());
				cd.setStandardInChI(stdInchi.getValue());
				cd.setStandardInChIKey(stdInchIKey.getValue());
				chemicalData.put(ne.getSurface(), cd);
			}

		}

		return chemicalData;

	}

	/**
	 * For every member of textLinesWithinPDF[], use OSCAR to find and resolved
	 * Named Entities, adding them to entities
	 */
	void generateEntities() {

		resolvedNamedEntities = new ArrayList<>();

		for (int i = 0; i < textLinesWithinPDF.length; i++) {

			resolvedNamedEntities.addAll(oscar
					.findAndResolveNamedEntities(textLinesWithinPDF[i]));

		}

	}

	public Set<String> getUniqueStandardInChIs() {

		Iterator<?> it = this.chemicalData.entrySet().iterator();
		Set<String> standardInChIs = new HashSet<>();

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, ChemicalDatum> pairs = (Map.Entry<String, ChemicalDatum>) it
					.next();

			standardInChIs.add(pairs.getValue().getStandardInChI());
			it.remove(); // avoids a ConcurrentModificationException
		}

		return standardInChIs;

	}

	public Set<String> getUniqueStandardInChIKeys() {

		Iterator<?> it = this.chemicalData.entrySet().iterator();
		Set<String> standardInChIKeys = new HashSet<>();

		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, ChemicalDatum> pairs = (Map.Entry<String, ChemicalDatum>) it
					.next();
			standardInChIKeys.add(pairs.getValue().getStandardInchiKey());

			it.remove(); // avoids a ConcurrentModificationException
		}
		return standardInChIKeys;

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

		// Extract ASCII text from PDF stream
		extractTextfromPDFStream(is);

		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Process the text string with OSCAR
		generateEntities();

		// Generate a unique set of chemicals from the PDF
		this.chemicalData = populateChemicalData();

	}

	/**
	 * Populate textLinesWithinPDF[] from the entire text in the PDF. This
	 * string array is split by new line. This essentially is a chunking method
	 * to allow OSCAR to work on small pieces.
	 * 
	 * It will also calculate the MD5SUM hash of the PDF.
	 * 
	 * @param is
	 */
	void extractTextfromPDFStream(FileInputStream is) {

		MessageDigest md = null;
		PDFTextStripper stripper = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();
		}

		// Wrap the FileInputStream so that we can MD5SUM it
		DigestInputStream dis = new DigestInputStream(is, md);

		try {

			doc = PDDocument.load(dis);

			byte[] md5Digest = dis.getMessageDigest().digest();
			this.md5SumOfPDFFile = Hex.encodeHexString(md5Digest);

			dis.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			stripper = new PDFTextStripper();
			// Split the string into lines by new line.
			// TODO this needs to be done by paragraph...
			textLinesWithinPDF = stripper.getText(doc).split("\\r?\\n");

			doc.close();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	FileInputStream getPDFStreamFromFileName(String fileName) {

		File file = new File(fileName);
		FileInputStream is = null;

		try {
			is = new FileInputStream(file);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return is;

	}

	public String getMd5Sum() {

		return md5SumOfPDFFile;
	}

}
