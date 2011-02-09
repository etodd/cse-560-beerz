import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import util.ByteOperations;

public class Parser {

	private InstructionDefinition[] instructionDefinitions;
	
	public Parser() {
		this.instructionDefinitions = new InstructionDefinition[] {
			new InstructionDefinition(
				"ADD",
				new int[] { 0x1000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 2, 0)
				}),
			new InstructionDefinition(
				"ADD",
				new int[] { 0x1020 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 4, 0)
				}),
			new InstructionDefinition(
				"AND",
				new int[] { 0x5000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 2, 0)
				}),
			new InstructionDefinition(
				"AND",
				new int[] { 0x5020 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 4, 0)
				}),
			new InstructionDefinition(
				"BRN",
				new int[] { 0x0800 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRZ",
				new int[] { 0x0400 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRP",
				new int[] { 0x0200 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRNZ",
				new int[] { 0x0C00 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRNP",
				new int[] { 0x0A00 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRZP",
				new int[] { 0x0600 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"BRNZP",
				new int[] { 0x0E00 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"DBUG",
				new int[] { 0x8000 },
				new OperandDefinition[] { }),
			new InstructionDefinition(
				"JSR",
				new int[] { 0x4000 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"JMP",
				new int[] { 0x4800 },
				new OperandDefinition[] {
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"JSRR",
				new int[] { 0xC000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"JMPR",
				new int[] { 0xC800 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"LD",
				new int[] { 0x2000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL, OperandType.LITERAL }, 8, 0)
				}),
			new InstructionDefinition(
				"LDI",
				new int[] { 0xA000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"LDR",
				new int[] { 0x6000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 5, 0)
				}),
			new InstructionDefinition(
				"LEA",
				new int[] { 0xE000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"NOT",
				new int[] { 0x9000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6)
				}),
			new InstructionDefinition(
				"RET",
				new int[] { 0xD000 },
				new OperandDefinition[] { }),
			new InstructionDefinition(
				"ST",
				new int[] { 0x3000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"STI",
				new int[] { 0xB000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(true, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 8, 0)
				}),
			new InstructionDefinition(
				"STR",
				new int[] { 0x7000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 11, 9),
					new OperandDefinition(false, new OperandType[] { OperandType.REGISTER, OperandType.SYMBOL }, 8, 6),
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 6, 0)
				}),
			new InstructionDefinition(
				"TRAP",
				new int[] { 0xF000 },
				new OperandDefinition[] {
					new OperandDefinition(false, new OperandType[] { OperandType.IMMEDIATE, OperandType.SYMBOL }, 7, 0)
				})
		};
	}
	
	public Program parse(String data) {
		String[] lines = data.split("\n");
		SymbolTable symbols = new SymbolTable();
		LiteralTable literals = new LiteralTable();
		List<Instruction> instructions = new LinkedList<Instruction>();
		int startAddress = 0;
		int location = 0;
		int origin = 0;
		int lineNumber = 1;
		for (String line : lines) {
			if (line.charAt(0) == ';')
				continue; // Skip comment lines
				
			try {
			
				String label = line.substring(0, 7).trim();
				String op = line.substring(9, 14).trim();
				String[] operands = this.getOperands(line);
				
				if (!label.equals("") && !op.equals(".EQU")) {
					symbols.define(new Symbol(label, location, true));
				}
				
				Instruction instruction = new Instruction(op, line);
				
				if (op.equals(".ORIG")) {
					instruction.setDefinition(new InstructionDefinition(".ORIG", 0));
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					origin = ByteOperations.parseHex(operands[0]);
				}
				else if (op.equals(".EQU")) {
					instruction.setDefinition(new InstructionDefinition(".EQU", 0));
					if (operands.length > 1 || operands.length == 0 || label == "") {
						// Error
					}
					OperandType type = Operand.determineType(operands[0]);
					if (type == OperandType.SYMBOL) {
						symbols.define(label, operands[0]);
					}
					else {
						symbols.define(new Symbol(label, Operand.parseConstant(operands[0]), false));
					}
				}
				else if (op.equals(".FILL")) {
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					instruction.setOperands(operands, literals, origin);
					instruction.setDefinition(new InstructionDefinition(".FILL", 1));
				}
				else if (op.equals(".STRZ")) {
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					String stringLiteral = operands[0];
					String[] chars = new String[stringLiteral.length() + 1];
					for (int i = 0; i < stringLiteral.length(); i++) {
						chars[i] = "x" + ByteOperations.getHex((int) stringLiteral.charAt(i), 4);
					}
					chars[stringLiteral.length()] = "x0000";
					instruction.setOperands(chars, literals, origin);
					instruction.setDefinition(new InstructionDefinition(".STRZ", stringLiteral.length() + 1));
				}
				else if (op.equals(".END")) {
					instruction.setDefinition(new InstructionDefinition(".END", 0));
					if (operands.length > 1) {
						// Error
					}
					if (operands.length == 1) {
						startAddress = Operand.getValue(operands[0], symbols, literals);
					}
				}
				else if (op.equals(".BLKW")) {
					instruction.setDefinition(new InstructionDefinition(".BLKW", 0));
					if (operands.length > 1 || operands.length == 0) {
						// Error
					}
					location += Operand.getValue(operands[0], symbols, literals);
				}
				else {
					instruction.setOperands(operands, literals, origin);
					InstructionDefinition definition = this.getInstructionDefinition(instruction);
					if (definition == null) {
						// Error
						System.out.println("Error: undefined operation \"" + op + "\"");
						definition = new InstructionDefinition(op, operands.length);
					}
					instruction.setDefinition(definition);
				}
				location += instruction.getDefinition().getSize();
				instructions.add(instruction);
			}
			catch (Exception e) {
				// Error handling
				System.out.println("Error on line " + Integer.toString(lineNumber) + ": ");
				e.printStackTrace();
			}
			lineNumber++;
		}
		return new Program(symbols, literals, instructions, startAddress, origin);
	}
	
	protected InstructionDefinition getInstructionDefinition(Instruction instruction) {
		for (InstructionDefinition definition : this.instructionDefinitions) {
			if (definition.isAcceptable(instruction)) {
				return definition;
			}
		}
		return null;
	}
	
	protected String[] getOperands(String line) {
		String trimmed = line.substring(17).trim();
		ArrayList<String> result = new ArrayList<String>();
		boolean inQuotes = false;
		String currentOperand = "";
		for(int i = 0; i < trimmed.length(); i++) {
			char c = trimmed.charAt(i);
			if (inQuotes) {
				if (c == '"') {
					inQuotes = false;
					result.add(currentOperand);
					currentOperand = "";
				}
				else {
					currentOperand += c;
				}
			}
			else {
				if (c == '"') {
					inQuotes = true;
				}
				else if(c == ';') {
					break;
				}
				else if (c == ',') {
					result.add(currentOperand.trim());
					currentOperand = "";
				}
				else {
					currentOperand += c;
				}
			}
		}
		if(!currentOperand.trim().equals("")) {
			result.add(currentOperand.trim());
		}
		String[] array = new String[result.size()];
		return result.toArray(array);
	}
}