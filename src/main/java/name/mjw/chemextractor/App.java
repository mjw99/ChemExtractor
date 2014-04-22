package name.mjw.chemextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ChemicalStructure;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.FormatType;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ResolvedNamedEntity;

public class App {
	PDDocument doc = null ;
	Oscar oscar = null;

	
	App(){
		 oscar = new Oscar();
	}
	
	public void main(String[] args) throws IOException {

	}

	public void processStringWithOscarToObtainInchi(String s) {

		
		List<ResolvedNamedEntity> entities = oscar.findAndResolveNamedEntities(s);
		for (ResolvedNamedEntity ne : entities) {
			//System.out.println(ne.getSurface());
			ChemicalStructure inchi = ne.getFirstChemicalStructure(FormatType.INCHI);
								
			if (inchi != null) {
				System.out.println(ne.getSurface() + "\t\t\t" + inchi );
			}
			
		}
	}
	
	public void processStringWithOscarToObtainCML(String s) {

		List<ResolvedNamedEntity> entities = oscar.findAndResolveNamedEntities(s);
		for (ResolvedNamedEntity ne : entities) {

			ChemicalStructure cml = ne.getFirstChemicalStructure(FormatType.CML);
			if (cml != null) {
				System.out.println(cml);
			}

		}
	}
	

	public void processPDFfromStream(FileInputStream input) {

		try {
			doc = PDDocument.load(input);
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
			String hyphenatedText = stripper.getText(doc);

			//A hack
			
			String text = hyphenatedText.replaceAll("-\r\n", "");
			
			processStringWithOscarToObtainInchi(text);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public FileInputStream processPDFfromFileName(String fileName) {
		File file = new File(fileName);
		FileInputStream is = null;

		try {
			is = new FileInputStream(file);
			processPDFfromStream(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;

	}

}
