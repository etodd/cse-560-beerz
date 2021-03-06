package Assembler;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.Comparator;
import java.util.Collections;
import Common.ByteOperations;
import Common.Error;
import Common.Symbol;
import Common.SymbolTable;

/**
 *  A Program contains an in-memory representation of an assembly Program, which can be rendered into binary form with the getCode function.
 *
 */
public class Program {
	
	/**
	 *  Contains the names and values of all symbols defined in this Program.
	 */
	private SymbolTable symbols;
	
	/**
	 *  Contains the addresses and values of all literals defined in this Program.
	 */
	private LiteralTable literals;
	
	/**
	 *  Contains all Instructions (source code, binary code, and Operand values) in this Program.
	 */
	private List<Instruction> instructions;
	
	/**
	 *  The address (relative to the origin) to start execution at.
	 */
	private int startAddress;
	
	/**
	 *  The absolute address of the first memory slot used by the Program. Any relocatable values in the Program are offset by this address.
	 */
	private int origin;
	
	/**
	 *  Name for the program. Used as the segment name in the header record.
	 */
	private String name;
	
	/**
	 *  True if the program is relocatable.
	 */
	private boolean isRelocatable;
	
	/**
	 *  Instantiates a new Program with the given data.
	 * @param name the name of the program
	 * @param isRelocatable a boolean that indicates whether the program is relocatable or not
	 * @param symbols a Symboltable of the program's contained symbols
	 * @param literals a LiteralTable of the program's contained literals
	 * @param instructions a List of the program's instructions
	 * @param startAddress the starting address of the program
	 * @param origin the origin of the program
	 */
	public Program(String name, boolean isRelocatable, SymbolTable symbols, LiteralTable literals, List<Instruction> instructions, int startAddress, int origin) {
		this.name = name;
		this.isRelocatable = isRelocatable;
		this.symbols = symbols;
		this.literals = literals;
		this.instructions = instructions;		
		this.startAddress = startAddress;
		this.origin = origin;
	}
	
	/**
	 *  Gets the object code for this Program, optionally displaying a listing for the user.
	 * @param printListing a boolean to determine whether or not to print a program listing
	 * @return a String representing the object file
	 */
	public String getCode(boolean printListing) throws Exception {
		
		StringBuffer output = new StringBuffer();
		
		StringBuffer result = new StringBuffer();
		
		String header = "H" + this.name;
		
		if (this.isRelocatable) {
			header += "MMMM";
		} else {
			header += ByteOperations.getHex(this.origin, 4);
		}
		header += ByteOperations.getHex(this.literals.getOffset() - this.origin + this.literals.getEntries().size(), 4);
		
		result.append(header);
		result.append("\r\n");
		
		int address = this.origin;
		
		List<Error> errors = new LinkedList<Error>();
		
		for (Symbol symbol : this.symbols.getSymbols()) {
			if (symbol.isExport()) {
				result.append("X");
				if (symbol.isRelocatable()) {
					result.append("R");
				} else {
					result.append("A");
				}
				result.append(ByteOperations.getHex(symbol.getValue(), 4));
				result.append(symbol.getName());
				result.append("\r\n");
			}
		}
		
		for (Instruction instruction : this.instructions) {
			for (Operand operand : instruction.getOperands()) {
				Symbol symbol = operand.getSymbol(this.symbols);
				if (symbol != null && symbol.isImport()) {
					OperandDefinition definition = operand.getDefinition();
					if (definition != null) {
						result.append("I");
						result.append(ByteOperations.getHex(address + definition.getOperationIndex(), 4));
						result.append(ByteOperations.getHex(definition.getMostSignificantBit(), 1));
						result.append(ByteOperations.getHex(definition.getLeastSignificantBit(), 1));
						result.append(symbol.getName());
						result.append("\r\n");
					}
				}
			}
			address += instruction.getDefinition().getSize();
		}
		
		if (this.startAddress < this.origin || this.startAddress > address)
			errors.add(new Error("Invalid start execution address."));
		address = this.origin;
		
		// Output instructions.
		for (Instruction instruction : this.instructions) {
			try {
				int[] codes = instruction.getCodes(this.symbols, this.literals);
				if (codes.length > 0) {
					int[] relocationMasks = instruction.getRelocationMasks(this.symbols);
					for (int i = 0; i < codes.length; i++) {
						String code = ByteOperations.getHex(address + i, 4) + ByteOperations.getHex(codes[i], 4);
						String line = code;
						if (i == 0) {
							line += "    " + instruction.getSource();
						}
						output.append(line + "\n");
						result.append("T");
						result.append(code);
						if (this.isRelocatable) {
							// For our purposes, we only have two types of relocatable addresses:
							// 9-bit page address offsets, and full sixteen bit addresses.
							if (ByteOperations.getBit(relocationMasks[i], 15)) {
								// It's a full sixteen bit modification record
								result.append("M1");
							}
							else if (ByteOperations.getBit(relocationMasks[i], 0)) {
								// It's a 9-bit page address offset.
								result.append("M0");
							}
						}
						result.append("\r\n");
					}
				}
				else {
					output.append("            " + instruction.getSource() + "\n");
				}
				address += instruction.getDefinition().getSize();
			}
			catch (Exception e) {
				if (e.getMessage() != null) {
					errors.add(new Error(e.getMessage()));
				} else {
					e.printStackTrace();
				}
			}
		}
		
		// Output literal table. 
		List list = new LinkedList(this.literals.getEntries());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
				.compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		Iterator<Map.Entry<Integer, Integer>> entryIterator = list.iterator();
		while (entryIterator.hasNext()) {
			Map.Entry<Integer, Integer> entry = entryIterator.next();
			String code = ByteOperations.getHex(entry.getValue() + this.literals.getOffset(), 4) + ByteOperations.getHex(entry.getKey(), 4);
			result.append("T");
			result.append(code);
			result.append("\r\n");
			output.append(code + "\n");
		}
		
		result.append("E" + ByteOperations.getHex(this.startAddress, 4));
		
		if (errors.size() > 0) {
			// Output each error message that we have encountered
			StringBuffer msg = new StringBuffer();
			for(Error e : errors) {
				msg.append("Assemble error: ");
				if(e.hasLineNumber()) {
					msg.append("Line ");
					msg.append(Integer.toString(e.getLineNumber()));
					msg.append(" - ");
				}
				msg.append(e.getMessage());
				msg.append("\n");
			}
			throw new Exception(msg.toString());
		} else if (printListing) {
			System.out.println(output);
		}
		
		// Return resulting code.
		return result.toString();
	}
}