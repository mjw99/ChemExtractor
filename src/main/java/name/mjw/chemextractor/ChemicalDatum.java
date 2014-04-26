package name.mjw.chemextractor;

import net.sf.jniinchi.JniInchiException;
import net.sf.jniinchi.JniInchiInput;
import net.sf.jniinchi.JniInchiInputInchi;
import net.sf.jniinchi.JniInchiOutput;
import net.sf.jniinchi.JniInchiOutputKey;
import net.sf.jniinchi.JniInchiOutputStructure;
import net.sf.jniinchi.JniInchiWrapper;

/**
 * Container class for a chemical.
 * Also contains additional metadata pertaining to that chemical. 
 * @author mw529
 *
 */
public class ChemicalDatum {

	/**
	 * Non IUPAC informal name.
	 * 
	 */
	private String name;
	/**
	 * @see <a href=http://en.wikipedia.org/wiki/International_Chemical_Identifier> InChI </a> 
	 */
	private String standardInChI;
	/**
	 * Hash of the Standard InChI value; use this for searching
	 * @see <a href=http://en.wikipedia.org/wiki/International_Chemical_Identifier#InChIKey> InChIKey </a> 
	 */
	private String standardInChIKey;
	/**
	 * CML form of the chemical.
	 */
	private String cml;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getStandardInChI() {
		return standardInChI;
	}

	/**
	 * Will autmatically translate a non-standard InChI to a standard one
	 * 
	 * @param inchi
	 *            non-standard InChI
	 */

	public void setInchi(final String inchi) {
		this.standardInChI = inchiToStandardInchi(inchi);

		JniInchiOutputKey key = null;

		try {
			key = JniInchiWrapper.getInchiKey(this.standardInChI);
		} catch (JniInchiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		standardInChIKey = key.getKey();

	}

	/**
	 * Converts an InChI to a Standard InChI
	 * 
	 * @param inchi
	 *            Non-standard InChI
	 * @return Standard InChI
	 */
	String inchiToStandardInchi(final String inchi) {

		String standardInchi = null;

		try {

			JniInchiInputInchi jniInchiInputInchi = new JniInchiInputInchi(
					inchi);
			JniInchiOutputStructure jniInchiOutputStructure = JniInchiWrapper
					.getStructureFromInchi(jniInchiInputInchi);
			JniInchiInput jniInchiInput = new JniInchiInput(
					jniInchiOutputStructure);
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

	public String getStandardInchiKey() {

		return standardInChIKey;
	}

}
