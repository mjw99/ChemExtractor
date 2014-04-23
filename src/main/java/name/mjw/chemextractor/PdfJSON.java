package name.mjw.chemextractor;


import java.util.HashMap;

public class PdfJSON {

	private String name;
	
	private String md5Sum;
	
	
	/**
	 * ChemicalData are unique as a function of name
	 */
	HashMap<String, ChemicalDatum> chemicalData = new HashMap<String, ChemicalDatum>();
	
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
	
}
