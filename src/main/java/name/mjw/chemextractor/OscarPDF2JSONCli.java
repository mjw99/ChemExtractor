package name.mjw.chemextractor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class OscarPDF2JSONCli {

	private final static String NAME = "OscarPDF2JSONCli";

	private final static String TITLE = NAME
			+ " - A tool for extracting Chemical terms from a PDF";
	
	private final static String EXAMPLE = "  "
			+ NAME.toLowerCase()
			+ " -i foo.pdf "
			+ "\n";
	
	
	private static OptionParser optionParser = null;
	
	private static String inputPDFFileName;
	
	private static OscarPDF2JSON oscarPDF2JSON = null;

	
	public static void main(String[] args) {

		// Populate the internal state of the object as directed by the command
		// line options
		parseArguments(args);
		
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(inputPDFFileName);
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file" + inputPDFFileName);
			e.printStackTrace();
		}
				
		oscarPDF2JSON = new OscarPDF2JSON(inputStream);
		System.out.println(oscarPDF2JSON.getJSON());	
		
		
	}
	

	private static void parseArguments(String[] args) {

		optionParser = getOptionParser();

		if (args.length == 0) {
			printUsage();
			System.exit(0);
		}

		try {
			processArguments(args);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException");
			e.printStackTrace();
		}
	}
	/**
	 * Action the command line arguments
	 * 
	 * @param args
	 *            Command line arguments
	 */
	private static void processArguments(String[] args) {

		OptionSet optionSet = optionParser.parse(args);

		if (optionSet.has("h")) {
			printUsage();
			System.exit(0);
		}
		
		inputPDFFileName = (String) optionSet.valueOf("i");

	}
	
	/**
	 * Sets up all the valid options for this command
	 * 
	 * @return OptionParser valid options
	 */
	private static OptionParser getOptionParser() {
		return new OptionParser() {
			{
				acceptsAll(asList("i", "inputPDFFileName"),
						"the input PDF file to be processed")
						.withRequiredArg().ofType(String.class).isRequired();


				acceptsAll(asList("h", "?", "help"), "display this output");

			}
		};

	}

	
	
	
	private static void printUsage() {
		System.out.println("");
		System.out.println(TITLE);
		System.out.println("");

		try {
			optionParser.printHelpOn(System.out);
		} catch (IOException e) {
			System.out.println("Cannot print help options to System.out");
			e.printStackTrace();
		}

		System.out.println("");
		System.out.println("Example usage:");
		System.out.println(EXAMPLE);
		System.exit(0);
	}
	
	@SafeVarargs
	private static <T> List<T> asList(T... items) {
		return Arrays.asList(items);
	}

}
