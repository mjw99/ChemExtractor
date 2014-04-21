package name.mjw.chemextractor;

import java.util.List;

import uk.ac.cam.ch.wwmm.oscar.Oscar;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ChemicalStructure;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.FormatType;
import uk.ac.cam.ch.wwmm.oscar.chemnamedict.entities.ResolvedNamedEntity;

public class App {
	public static void main(String[] args) {
		String s = "Benzene, Phenol, water";

		Oscar oscar = new Oscar();
		List<ResolvedNamedEntity> entities = oscar
				.findAndResolveNamedEntities(s);
		for (ResolvedNamedEntity ne : entities) {
			System.out.println(ne.getSurface());
			ChemicalStructure inchi = ne
					.getFirstChemicalStructure(FormatType.INCHI);
			if (inchi != null) {
				System.out.println(inchi);
			}
			System.out.println();
		}
	}
}
