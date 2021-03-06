package Simulator.state;
import java.io.PrintStream;
import Simulator.instructions.DebugHandler;

/**
 * This class represents the state of a virtual machine, not including the memory, which is represented by a MemoryBank.
 */
public class MachineState {
	/**
	 * The number of registers supported by all Machines.
	 */
	public static final int NUM_REGISTERS = 8;

	/**
	 * Represents the negative condition code register.
	 * True if and only if the last written register is negative.
	 */
	public boolean ccrNegative = false;
	
	/**
	 * Represents the positive condition code register.
	 * True if and only if the last written register is positive.
	 */
	public boolean ccrPositive = false;
	
	/**
	 * Represents the zero condition code register.
	 * True if and only if the last written register is zero.
	 */
	public boolean ccrZero = true;
	
	/**
	 * True if and only if the machine is actively executing.
	 */
	public boolean executing = true;
	
	/**
	 * Array representing all the registers in the machine. The initial value of all registers is zero.
	 */
	public short[] registers = new short[MachineState.NUM_REGISTERS];
	
	/**
	 * Represents the program counter register.
	 */
	public int programCounter;
	
	/**
	 * Gets a copy of this MachineState.
	 * @return A copy of this MachineState.
	 */
	public MachineState clone() {
		MachineState x = new MachineState();
		x.ccrNegative = this.ccrNegative;
		x.ccrPositive = this.ccrPositive;
		x.ccrZero = this.ccrZero;
		x.programCounter = this.programCounter;
		x.executing = this.executing;
		for(int i = 0; i < MachineState.NUM_REGISTERS; i++) {
			x.registers[i] = this.registers[i];
		}
		return x;
	}
	
	/**
	 * Updates the CCR register in accordance with the given signed 16-bit value.
	 * If the value is zero, only the zero bit will be on.
	 * If the value is positive, only the positive bit will be on.
	 * If the value is negative, only the negative bit will be on.
	 * @param value The signed 16-bit value to use when updating the CCR.
	 */
	public void updateCcr(short value) {
		if (value == 0) {
			this.ccrZero = true;
			this.ccrPositive = false;
			this.ccrNegative = false;
		}
		else {
			if (value > 0) {
				this.ccrPositive = true;
				this.ccrZero = false;
				this.ccrNegative = false;
			}
			else {
				if (value < 0) {
					this.ccrNegative = true;
					this.ccrZero = false;
					this.ccrPositive = false;
				}
			}
		}
	}
	
	/**
	 * Outputs this MachineState to the given IO stream according to the DEBUG
	 * instruction specifications.
	 */
	public void display(PrintStream output) {
		new DebugHandler().execute(output, 0, this, null);
	}
}