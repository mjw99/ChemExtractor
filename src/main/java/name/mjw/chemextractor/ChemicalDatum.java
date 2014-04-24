package name.mjw.chemextractor;


import net.sf.jniinchi.JniInchiException;
import net.sf.jniinchi.JniInchiInput;
import net.sf.jniinchi.JniInchiInputInchi;
import net.sf.jniinchi.JniInchiOutput;
import net.sf.jniinchi.JniInchiOutputKey;
import net.sf.jniinchi.JniInchiOutputStructure;

import net.sf.jniinchi.JniInchiStructure;
import net.sf.jniinchi.JniInchiWrapper;






public class ChemicalDatum {

	private String name;
	private String inchi;
	private String inchiKey;
	private String cml;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getInchi() {
		return inchi;
	}

	public void setInchi(final String inchi) {
		this.inchi = inchiToStandardInchi(inchi);

		JniInchiOutputKey key = null;

		try {
			key = JniInchiWrapper.getInchiKey(this.inchi);
		} catch (JniInchiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		inchiKey = key.getKey();

	}

	/**
	 * Converts an InChI to a Standard InChI
	 * @param inchi
	 * @return
	 */
	String inchiToStandardInchi(String inchi) {

		String standardInchi = null;


		try {

			JniInchiInputInchi jniInchiInputInchi = new JniInchiInputInchi(inchi);
			JniInchiOutputStructure jniInchiOutputStructure = JniInchiWrapper.getStructureFromInchi(jniInchiInputInchi);
			JniInchiInput jniInchiInput = new JniInchiInput(jniInchiOutputStructure);
			JniInchiOutput output = JniInchiWrapper.getStdInchi(jniInchiInput);
			standardInchi = output.getInchi();

		} catch (JniInchiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return standardInchi;

	}

	public String getCml() {
		return cml;
	}

	public void setCml(final String cml) {
		this.cml = cml;
	}

	public String getInchiKey() {

		return inchiKey;
	}

}
