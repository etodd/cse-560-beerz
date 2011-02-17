package Assembler;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

// Main program.
public class Main {

	// Program entry point.
	public static void main(String[] args) {
		// Print usage data if necessary
		if (args.length < 1 || args[0].equals("--help") || args[0].equals("/?")) {
			Main.printUsageInformation();
			return;
		}
		
		String filename = args[0];
		String outFile = null;
		boolean generateListing = false;
		
		for (int i = 1; i < args.length; i++) {
			if (args[i].equals("-l")) {
				// Generate a listing
				generateListing = true;
			}
			else if (args[i].equals("-o")) {
				// Set the output file
				i++;
				if (i < args.length) {
					outFile = args[i];
				}
				else {
					Main.printUsageInformation();
					return;
				}
			}
			else {
				Main.printUsageInformation();
				return;
			}
		}
		
		try {
			String data = Main.readAllText(filename);
			Assembler assembler = new Assembler();
			Program program = assembler.assemble(filename, data);
			String result = program.getCode(generateListing);
			Main.writeAllText(outFile, result);
		}
		catch (IOException e) {
			System.out.println("Failed to assemble program due to an IO error.");
			return;
		}
	}
	
	// Prints usage information for users of this program.
	private static void printUsageInformation() {
		System.out.println("Usage:\tjava Main inputfile [options]");
		System.out.println("\t-o outputfile\tSpecify name of output object file.");
		System.out.println("\t-l\t\tGenerate and display source code listing.");
	}
	
	// Returns all text in the file existing at the given path location.
	private static String readAllText(String filename) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
		int numRead = 0;
		String line = null;
		while ((line = reader.readLine()) != null) {
			fileData.append(line);
			fileData.append("\n");
		}
        reader.close();
        return fileData.toString();
    }
	
	// Writes all given text to the file existing at the given path location.
	// If the file already exists, it is overwritten. If not, it is created.
	private static void writeAllText(String filename, String data) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		out.write(data);
		out.close();
    }
}