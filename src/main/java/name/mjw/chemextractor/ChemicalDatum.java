package name.mjw.chemextractor;

/**
 * Container class for a chemical. Also contains additional metadata pertaining
 * to that chemical.
 * 
 * @author mw529
 * 
 */
public class ChemicalDatum {

	/**
	 * Non IUPAC informal name of the chemical.
	 * 
	 */
	private String name;

	/**
	 * InChI is defined as “a series of characters derived by applying a set of
	 * rules to a chemical structure to provide a unique digital ‘signature’ for
	 * a compound.”
	 * 
	 * Standard InChI was defined to ensure interoperability/compatibility
	 * between large databases/web searching & information exchange. It is a
	 * subset of InChI.
	 * 
	 * @see <a
	 *      href=http://en.wikipedia.org/wiki/International_Chemical_Identifier>
	 *      InChI </a>
	 */
	private String standardInChI;

	/**
	 * Hash of the Standard InChI value; use this for searching
	 * 
	 * @see <a
	 *      href=http://en.wikipedia.org/wiki/International_Chemical_Identifier
	 *      #InChIKey> InChIKey </a>
	 * 
	 *      The InChIKey is a fixed length SHA-256 hash of InChI (27 characters,
	 *      including two hyphens). Its fixed length makes it easy to index and
	 *      it is thus designed for databases and web searching.
	 */
	private String standardInChIKey;

	/**
	 * Chemical Markup Language (CML) form of the chemical.
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
	 * Sets the standard InChI value.
	 * 
	 * @param stdInchi
	 *            Standard InChI
	 */
	public void setStandardInChI(final String stdInchi) {

		this.standardInChI = stdInchi;

	}
	
	/**
	 * Sets the standard InChI value.
	 * 
	 * @param stdInchi
	 *            Standard InChI
	 */
	public void setStandardInChIKey(final String stdInchIKey) {

		this.standardInChIKey = stdInchIKey;

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
