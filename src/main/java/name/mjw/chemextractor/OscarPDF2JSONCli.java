package name.mjw.chemextractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;


import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class OscarPDF2JSONCli {

	private final static String NAME = "OscarPDF2JSON";

	private final static String TITLE = NAME
			+ " - A tool for extracting Chemical terms from a PDF";
	
	private final static String EXAMPLE = "  "
			+ NAME.toLowerCase()
			+ " foo.pdf bar.pdf "
			+ "\n";
	
	
	private static OptionParser optionParser = null;
	
	private static List<File> inputPDFFiles;
	
	private static OscarPDF2JSON oscarPDF2JSON = null;

	
	public static void main(String[] args) {

		// Populate the internal state of the object as directed by the command
		// line options
		parseArguments(args);

		oscarPDF2JSON = new OscarPDF2JSON();

		FileInputStream inputStream = null;
				
		List<PdfJSON> pdfJSONs = new ArrayList<PdfJSON>();
		
		try {
			for (File inputPDFFile : inputPDFFiles) {

				inputStream = new FileInputStream(inputPDFFile);

				oscarPDF2JSON.processfromPDFStream(inputStream);
				
				pdfJSONs.add(oscarPDF2JSON.getPdfJSON());

			}

		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file" + inputPDFFiles);
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		
		System.out.println(gson.toJson(pdfJSONs));

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
		
		inputPDFFiles = (List<File>) optionSet.nonOptionArguments();

	}

	/**
	 * Sets up all the valid options for this command
	 * 
	 * @return OptionParser valid options
	 */
	private static OptionParser getOptionParser() {

		OptionParser parser = new OptionParser();

		parser.nonOptions("PDF File(s) to be processed").ofType(File.class);

		parser.acceptsAll(asList("h", "?", "help"), "display this output");

		return parser;

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
