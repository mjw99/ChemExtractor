package name.mjw.chemextractor;

import java.util.HashMap;

/**
 * A container class for a chemically processed PDF which is used to generate JSON.
 * 
 * @author mw529
 * 
 */
public class PdfJSON {

	private String name;

	private String md5Sum;

	/**
	 * ChemicalData are unique as a function of name
	 */
	private HashMap<String, ChemicalDatum> chemicalData = new HashMap<String, ChemicalDatum>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMd5Sum() {
		return md5Sum;
	}

	public void setMd5Sum(String md5Sum) {
		this.md5Sum = md5Sum;
	}

	public HashMap<String, ChemicalDatum> getChemicalData() {
		return chemicalData;
	}

	public void setChemicalData(HashMap<String, ChemicalDatum> chemicalData) {
		this.chemicalData = chemicalData;
	}

}
