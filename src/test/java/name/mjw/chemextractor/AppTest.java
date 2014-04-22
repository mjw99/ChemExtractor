package name.mjw.chemextractor;


import org.junit.Test;

public class AppTest {

	@Test
	public void processStringWithOscar() {
		App app = new App();

		app.processStringWithOscar("Benzene, car, television, phenol");
	}

}
