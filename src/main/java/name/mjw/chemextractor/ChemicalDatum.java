package name.mjw.chemextractor;

import net.sf.jniinchi.JniInchiException;
import net.sf.jniinchi.JniInchiOutputKey;
import net.sf.jniinchi.JniInchiWrapper;

public class ChemicalDatum {

	private String name;
	private String inchi;
	private String inchiKey;
	private String cml;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInchi() {
		return inchi;
	}

	public void setInchi(String inchi) {
		this.inchi = inchi;

		JniInchiOutputKey key = null;

		try {
			key = JniInchiWrapper.getInchiKey(inchi);
		} catch (JniInchiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		inchiKey = key.getKey();

	}

	public String getCml() {
		return cml;
	}

	public void setCml(String cml) {
		this.cml = cml;
	}

	public String getInchiKey() {

		return inchiKey;
	}

}
